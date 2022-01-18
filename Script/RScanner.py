import xml.etree.ElementTree as ET

import subprocess
import sys

from lxml import etree
import pprint
import json


ScriptVersion = "0.3"

# TO DO 
# method invocations of classname::methodname to be prefixed with classname <SYMBOL_PACKAGE>
#

# Debug routine to print the XML Path 
def printpath(fname):
    xmldoc = etree.parse(open(fname))
    for node in xmldoc.xpath('//*'):
        if not node.getchildren() and node.text:
            print(xmldoc.getpath(node) + " " + node.text)


# Get the list of built in keywords by parsing the R command output in 'builtin.log'
def getBuiltIns():
    result = []
    with open('builtin.log') as f:
        for line in f:
            flist = line.split()
            result.append(flist[1].replace('\"',''))
            result.append(flist[2].replace('\"',''))
    return result

builtins = getBuiltIns()
        



# lines of comment
def getNumComments(root):
    linecount=0
    for c in root.findall('.//COMMENT'):
        #print(c.text)
        #print(c.get('line1'))
        #print(c.get('line2'))
        linecount += 1
    return(linecount)

# Get lines of code 
def getNumLOC(root):
    elist = []
    for c in root.findall('.//expr'):
        elist.append(c.get('line1'))

    elist = list(set(elist))
    return (len(elist))

r6_keywords = ['R6Class','if_any','map','reset','new']
s4_keywords = ['setMethod','slot','setClass','representation','selectMethod','setGeneric','signature']

#------
# get the function calls 
#-------------
def getFunctionCalls(root):
    result = []
    for c in root.findall('.//SYMBOL_FUNCTION_CALL'):
        if (c.text in builtins) or (c.text in r6_keywords) or (c.text in s4_keywords) :
            pass
        else:
            result.append(c.text)
    return (list(set(result)))

#-----------
# get the function definitions
#------------
def getFunctionDefinitions(root):
    result = []
    for gp in root.findall('.//FUNCTION/../..'):
        
        etags,etxt,elist = getChildList(gp)
        if (etags == ['expr', 'LEFT_ASSIGN', 'expr']):
            lhs = elist[0]
            sym = findChild(lhs,'SYMBOL')
            result.append(sym)
    return (list(set(result)))
        
#-----------
# get the list of library packages
#-----------
def getLibPkg(root):
    result = []
    # get the grandparent expr of SYMBOL_FUNCTION_CALL
    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'library\']/../..'):
        etags,etxt,elist = getChildList(item)
        if (etags == ['expr', 'OP-LEFT-PAREN', 'expr','OP-RIGHT-PAREN']):
            sym = findChild(elist[2],'SYMBOL')
            result.append(sym)

    return (list(set(result)))
 
#-----------
# XML Parse - find the list indices of functions matching the list of patterns
#-----------
def findSublistIndx(sl,l):
    results=[]
    sl_len=len(sl)
    for ind in (i for i,e in enumerate(l) if e==sl[0]):
        if l[ind:ind+sl_len]==sl:
            results.append((ind,ind+sl_len-1))

    # return a list of tuple positions 
    return results

#-----------
# XML Parse - get the list of children , return elements,tags ,text
#-----------
def getChildList(item):
    elist = list(item.iterfind('*'))
    etags = [elem.tag for elem in elist]
    etxt = [elem.text for elem in elist]

    return etags,etxt,elist

#-----------
# XML Parse - find a child with specific tag
#-----------
def findChild(item,tag):
    result = list(item.iterfind(tag))
    if (len(result)):
        return(result[0].text)
    else:
        return ""

#-----------
# XML Parse - find a grndchild with specific tag
#-----------
def findGrandChildren(item,tag):
    children = list(item.iterfind('*'))
    result = [] 
    for item in children:
        txt = findChild(item,tag)
        if txt != "":
            result.append(txt)
    return result
#-----------
# XML Parse - find the children matching tag , return first item
#-----------
def findChildItem(item,tag):
    result = list(item.iterfind(tag))
    if (len(result)):
        return(result[0])
    else:
        return ""

#-----------
# XML Parse - find the children matching tag , return list
#-----------
def findChildren(item,tag):
    result = list(item.iterfind(tag))
    if (len(result)):
        return(result)
    else:
        return []


#-----------
# 
# Returns a list of class definitions of R6 class Type 
#-----------

def processR6ClassSeq(item):

    result = {} 
    result_count = {}
 
    etags,etxt,elist = getChildList(item)
    if (etags == ['expr', 'LEFT_ASSIGN', 'expr']):
        lhs = elist[0]
        rhs = elist[2]

    sym = findChild(lhs,'SYMBOL')
    #print(sym)

    etags,etxt,elist = getChildList(rhs)

    members = {}
    count = {} # dictionary to hold the count of methods , fields 
    indxs = findSublistIndx(['SYMBOL_SUB', 'EQ_SUB', 'expr'],etags)
    #print(indxs)
    for i in range(len(indxs)):
        memlist = []

        a,b = indxs[i]
        mtype = etxt[a]    # public , private or active declarations

        if (mtype == 'inherit'):
            subclass = findChild(elist[b],'SYMBOL')
            memlist.append(subclass)

        tag,txt,el = getChildList(elist[b])
        method_cnt = 0
        field_cnt = 0 

        i2 = findSublistIndx(['SYMBOL_SUB', 'EQ_SUB', 'expr'],tag)
        for i in range(len(i2)):
            a,b = i2[i]
            tag3,txt3,el3 = getChildList(el[b])
            if (tag3[0] == 'FUNCTION'):
                #print(f"Method    :  {txt[a]}")
                memlist.append(txt[a]+"()")
                method_cnt = method_cnt + 1
            else:
                #print(f"Member    :  {txt[a]}")
                field_cnt = field_cnt +1 
                memlist.append(txt[a])

        spl_attrs = ['classname','lock_objects','class','portable','lock_class','cloneable']
        if (mtype not in  spl_attrs):
            members[mtype] = memlist 
            count[mtype] = (field_cnt,method_cnt)

    result[sym] = members
    result_count[sym] = count 
    #print(result)
    # Returns two dictionaries
    return result , result_count



#-----------
# get the class definitions
# calls processR6 
#-----------

def getClassDef(root):

    result = []
    result_count = []
    # get the grandparent expr of SYMBOL_FUNCTION_CALL
    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'R6Class\']/../../..'):
        f,c = processR6ClassSeq(item)
        result.append(f)
        result_count.append(c)

    # Return the list of functions and count 
    return result , result_count


def stringWithoutQuote(str):
    return str.replace('"','')

# TO DO S4 - Parse setClassUnion
#https://r6.r-lib.org/reference/R6Class.html 

def getbasefile(filepath):
    filepath = filepath.split(".")[0]
    flist = filepath.split("/")
    if (len(flist) == 2):
        return flist[1]
    else:
        return flist[0]

def printlist(hdr,plist,f):
    f.writelines(hdr+"\n")
    for i in range(len(plist)):
        f.writelines(f"\t {plist[i]}\n")
    f.write("\n")

def printdict(hdr,pdictlist,f):
    f.writelines(hdr+"\n")

    dict_str = pprint.pformat(pdictlist)
    f.writelines(f"\t {dict_str}\n")
    
    f.write("\n")

def intersection(lst1, lst2):
    return list(set(lst1) & set(lst2))

def processxml(filename,actualfilename=""):

    metrics = {}

    if (actualfilename):
        fname = actualfilename
        basefile = getbasefile(actualfilename)
    else:
        fname = filename
        basefile = getbasefile(filename)


    basefile = getbasefile(fname)

    #print("-"*50)
    tree = ET.parse(filename)
    root = tree.getroot()


    loc = getNumLOC(root)

    fcalls = getFunctionCalls(root)
    fdef = getFunctionDefinitions(root)

    ifuncs = intersection(fdef,fcalls)

    numcalls = len(fcalls)
    internalcalls = len(ifuncs)
    externalcalls = (numcalls - internalcalls)

 

    lpkg = getLibPkg(root)
    
    cdef , count = getClassDef(root)

    #getS4ClassDef(root)

    total_pub_fields = 0 
    total_pri_fields = 0 

    total_pub_methods = 0 
    total_pri_methods = 0 

    dam = 0 

    for i in range(len(cdef)):
        cname = list(cdef[i].keys())[0]
        val = count[i][cname]

        if ("public" in val.keys()):
            pnum = val["public"]
            
            total_pub_fields  = total_pub_fields  + pnum[0] 
            total_pub_methods  = total_pub_methods  + pnum[1] 
        

        if ("private" in val.keys()):
            pnum = val["private"]
            
            private_fields = pnum[0]
            total_pri_fields  = total_pri_fields  + pnum[0] 
            total_pri_methods  = total_pri_methods  + pnum[1] 
        

        if (total_pri_fields + total_pub_fields):
            dam = total_pri_fields / (total_pri_fields + total_pub_fields)

    #TO DO METRICS 
    # Complexity AMC , WMC
    # RFC ?? LCOM ?? 
    # Cohesion CBO,Ca,Ce,LCOM,CAM , AMC,MI

    metrics["filename"] = actualfilename
    metrics["LOC"] = loc
    metrics["NMC"] = numcalls
    metrics["NMCI"] = internalcalls
    metrics["NMCE"] = externalcalls
    metrics["NOF"] = total_pub_fields
    metrics["NPM"] = total_pub_methods
    metrics["NPRIF"] = total_pri_fields
    metrics["NPRIM"] = total_pri_methods
    metrics["DAM"] = dam

    return metrics


#inpfiles = [('xml/classR6.xml','src/classR6.R'),('xml/infoGene.xml','src/infoGene.R'),('xml/classex1.xml','src/classex.R')]

def main():

    result = {}

    metrics = []
    result["ScriptVersion"] = ScriptVersion

    if __name__ == "__main__":
        #print(f"Arguments count: {len(sys.argv)}")
        for i, arg in enumerate(sys.argv):
            if (i>0):
                rscript = ["Rscript","RParse.R",arg,"temp.xml"]
                #print(rscript)
                subprocess.run(rscript)
                m = processxml("temp.xml",arg)
                metrics.append(m)
    

    result["metrics"] = metrics

    with open('metrics.json', 'w') as fp:
        json.dump(result, fp)


main()
