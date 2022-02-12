import xml.etree.ElementTree as ET
import subprocess
import sys
from lxml import etree
import pprint
import json
import re

# metrics http://www.virtualmachinery.com/sidebar3.htm

ScriptVersion = "0.4"


# TO DO
# method invocations of classname::methodname to be prefixed with classname <SYMBOL_PACKAGE>
#

def printpath(fname):
    """
    Debug routine to print the XML Path
    :param fname:
    :type fname:
    :return: None
    :rtype: None
    """
    xmldoc = etree.parse(open(fname))
    for node in xmldoc.xpath('//*'):
        if not node.getchildren() and node.text:
            print(xmldoc.getpath(node) + " " + node.text)


def getBuiltIns():
    """
    Get the list of built-in keywords by parsing the R command output in 'builtin.log'
    :return: List of built-in keywords
    :rtype: List
    """
    result = []
    with open('builtin.log') as f:
        for line in f:
            flist = line.split()
            result.append(flist[1].replace('\"', ''))
            result.append(flist[2].replace('\"', ''))
    return result


builtins = getBuiltIns()


def getNumComments(root):
    """
    Gets Lines of comments in code
    :param root: Root node of an XML Element tree
    :type root: XML ElementTree root node
    :return: Number of lines of comments
    :rtype: Int
    """
    linecount = 0
    for c in root.findall('.//COMMENT'):
        # print(c.text)
        # print(c.get('line1'))
        # print(c.get('line2'))
        linecount += 1
    return (linecount)


def getNumLOC(root):
    """
    Gets Lines of code
    :param root: Root node of an XML Element tree
    :type root: XML ElementTree root node
    :return: Number of lines of code
    :rtype: Int
    """
    elist = []
    for c in root.findall('.//expr'):
        elist.append(c.get('line1'))

    elist = list(set(elist))
    return (len(elist))


r6_keywords = ['R6Class', 'if_any', 'map', 'reset', 'new']
s4_keywords = ['setMethod', 'slot', 'setClass', 'representation', 'selectMethod', 'setGeneric', 'signature']


def getFunctionCalls(root):
    """
    Gets a List of function calls
    :param root: Root node of an XML Element tree
    :type root: XML ElementTree root node
    :return: Function Calls
    :rtype: List
    """
    result = []
    for c in root.findall('.//SYMBOL_FUNCTION_CALL'):
        if (c.text in builtins) or (c.text in r6_keywords) or (c.text in s4_keywords):
            pass
        else:
            result.append(c.text)
    return (list(set(result)))


# -----------
# get the function definitions
# ------------
def getFunctionDefinitions(root):
    """
    Gets a list of function definitions
    :param root: Root node of an XML Element tree
    :type root: XML ElementTree root node
    :return: Function Definitions
    :rtype: List
    """
    result = []
    for gp in root.findall('.//FUNCTION/../..'):

        etags, etxt, elist = getChildList(gp)
        if etags == ['expr', 'LEFT_ASSIGN', 'expr']:
            lhs = elist[0]
            sym = findChild(lhs, 'SYMBOL')
            result.append(sym)
    return list(set(result))


def getLibPkg(root):
    """
    Gets Library packages used in the code.
    :param root: Root node of an XML Element tree
    :type root: XML ElementTree root node
    :return: Library Packages
    :rtype: List
    """
    result = []
    # get the grandparent expr of SYMBOL_FUNCTION_CALL
    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'library\']/../..'):
        etags, etxt, elist = getChildList(item)
        if etags == ['expr', 'OP-LEFT-PAREN', 'expr', 'OP-RIGHT-PAREN']:
            sym = findChild(elist[2], 'SYMBOL')
            result.append(sym)

    return list(set(result))


def findSublistIndx(sl, l):
    """
    find the list indices of functions matching the list of patterns
    :param sl: SubList
    :type sl: List
    :param l:
    :type l: List
    :return: Tuple positions
    :rtype: List
    """
    results = []
    sl_len = len(sl)
    for ind in (i for i, e in enumerate(l) if e == sl[0]):
        if l[ind:ind + sl_len] == sl:
            results.append((ind, ind + sl_len - 1))

    # return a list of tuple positions 
    return results


def getChildList(item):
    """
    Get a list of an item's children
    :param item:
    :type item: Node
    :return: Elements, Tag, and Text
    :rtype:
    """
    elist = list(item.iterfind('*'))
    etags = [elem.tag for elem in elist]
    etext = [elem.text for elem in elist]

    return etags, etext, elist


# -----------
# XML Parse - find a child with specific tag
# -----------
def findChild(item, tag):
    """

    :param item:
    :type item:
    :param tag:
    :type tag:
    :return:
    :rtype:
    """
    result = list(item.iterfind(tag))
    if (len(result)):
        return (result[0].text)
    else:
        return ""


# -----------
# XML Parse - find a child with specific tag
# -----------
def findChildrenWithTag(item, tag):
    result = list(item.iterfind(tag))
    for i in item.findall(tag):
        ET.dump(i)
    # print(result)
    lst = []
    if (len(result)):
        # print(len(result))
        for i in range(len(result)):
            # print(result[i].text)
            lst.append(result[i].text)
        # print(lst)

        return (lst)
    else:
        return ""


# -----------
# XML Parse - find a grndchild with specific tag
# -----------
def findGrandChildren(item, tag):
    children = list(item.iterfind('*'))
    result = []
    for item in children:
        txt = findChild(item, tag)
        if txt != "":
            result.append(txt)
    return result


# -----------
# XML Parse - find the children matching tag , return first item
# -----------
def findChildItem(item, tag):
    result = list(item.iterfind(tag))
    if (len(result)):
        return (result[0])
    else:
        return ""


# -----------
# XML Parse - find the children matching tag , return list
# -----------
def findChildren(item, tag):
    result = list(item.iterfind(tag))
    if (len(result)):
        return (result)
    else:
        return []


def getCallList(itemlist):
    calllist = []
    for item in itemlist:
        calls = getFunctionCalls(item)
        calllist.extend(calls)

    # print(calllist)
    return calllist


def getAccessList(itemlist):
    result = []
    i = 0
    for item in itemlist:

        for id in item.findall('.//expr'):
            etags, etxt, elist = getChildList(id)
            indxs = findSublistIndx(['OP-DOLLAR', 'SYMBOL'], etags)
            for i in range(len(indxs)):
                a, b = indxs[i]
                result.append(etxt[a + 1])

    return list(set(result))


def sublist(lst1, lst2):
    ls1 = [element for element in lst1 if element in lst2]
    ls2 = [element for element in lst2 if element in lst1]
    return ls1 == ls2


def getObjectFields(item):
    objlist = []
    if (item.tag == "expr"):
        etags, etxt, elist = getChildList(item)
        for i in range(len(elist)):
            if (elist[i].tag == "expr"):
                et2, txt2, el2 = getChildList(elist[i])

                if (et2 == ['expr', 'LEFT_ASSIGN', 'expr']):
                    et3, txt3, el3 = getChildList(el2[2])

                    if (sublist(['expr', 'OP-LEFT-PAREN'], et3)):
                        et4, txt4, el4 = getChildList(el3[0])

                        indxs = findSublistIndx(['expr', 'OP-DOLLAR', 'SYMBOL_FUNCTION_CALL'], et4)

                        for i in range(len(indxs)):
                            a, b = indxs[i]
                            obj = findChild(el4[a], 'SYMBOL')
                            objlist.append(obj)

    # print(objlist)
    return objlist


# -----------
# 
# Returns a list of class definitions of R6 class Type 
# -----------

def processR6ClassSeq(item):
    result = {}
    result_count = {}

    etags, etxt, elist = getChildList(item)
    if (etags == ['expr', 'LEFT_ASSIGN', 'expr']):
        lhs = elist[0]
        rhs = elist[2]

        sym = findChild(lhs, 'SYMBOL')
        # print(sym)

        etags, etxt, elist = getChildList(rhs)

    members = {}
    accessors = {}
    methodacc = {}
    count = {}  # dictionary to hold the count of methods , fields
    indxs = findSublistIndx(['SYMBOL_SUB', 'EQ_SUB', 'expr'], etags)
    # print(indxs)
    for i in range(len(indxs)):
        inhlist = []

        a, b = indxs[i]
        mtype = etxt[a]  # public , private or active declarations

        if (mtype == 'inherit'):
            subclass = findChild(elist[b], 'SYMBOL')
            inhlist.append(subclass)

        tag, txt, el = getChildList(elist[b])
        method_cnt = 0
        field_cnt = 0

        memdict = {}
        memfunc = []
        memflds = []

        calllist = []
        objfields = []

        i2 = findSublistIndx(['SYMBOL_SUB', 'EQ_SUB', 'expr'], tag)
        for i in range(len(i2)):
            a, b = i2[i]
            line1 = el[b].get("line1")
            line2 = el[b].get("line2")
            tag3, txt3, el3 = getChildList(el[b])
            if (tag3[0] == 'FUNCTION'):
                fname = txt[a]
                # print(f"Method    :  {fname} {line1} {line2}")
                calllist.append(fname)
                memfunc.append((fname, line1, line2))
                method_cnt = method_cnt + 1
                calls = getCallList(el3)
                alist = getAccessList(el3)
                methodacc[fname] = alist
                calllist.extend(calls)

                for a in alist:
                    if a in accessors.keys():
                        accessors[a].append(fname)

                if (fname == "initialize"):
                    for j in range(len(el3)):
                        # ET.dump(el3[j])
                        objfields.extend(getObjectFields(el3[j]))
                # print(calls)
            else:
                # print(f"Member    :  {txt[a]}")
                field_cnt = field_cnt + 1
                memflds.append(txt[a])

                accessors[txt[a]] = []

        # print(accessors)
        calllist = list(set(calllist))
        memdict["methods"] = memfunc
        memdict["fields"] = memflds
        memdict["methodcalls"] = calllist
        memdict["access"] = accessors  # this dictionary has fields to method mapping
        memdict["methodaccess"] = methodacc  # this dictionary has method to fields access
        spl_attrs = ['classname', 'lock_objects', 'class', 'portable', 'lock_class', 'cloneable']

        if (len(objfields)):
            members["objects"] = objfields

        if (mtype == "inherit"):
            members[mtype] = inhlist
        elif (mtype not in spl_attrs):
            members[mtype] = memdict
            count[mtype] = (field_cnt, method_cnt)

    result[sym] = members
    result_count[sym] = count
    # print(result)
    # Returns two dictionaries
    return result, result_count


# -----------
# get the class definitions
# calls processR6 
# -----------

def getClassDef(root):
    result = []
    result_count = []
    # get the grandparent expr of SYMBOL_FUNCTION_CALL
    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'R6Class\']/../../..'):
        f, c = processR6ClassSeq(item)
        result.append(f)
        result_count.append(c)

    # Return the list of functions and count 
    return result, result_count


def stringWithoutQuote(str):
    return str.replace('"', '')


# TO DO S4 - Parse setClassUnion
# https://r6.r-lib.org/reference/R6Class.html

def getbasefile(filepath):
    filepath = filepath.split(".")[0]
    flist = filepath.split("/")
    if (len(flist) == 2):
        return flist[1]
    else:
        return flist[0]


def printlist(hdr, plist, f):
    f.writelines(hdr + "\n")
    for i in range(len(plist)):
        f.writelines(f"\t {plist[i]}\n")
    f.write("\n")


def printdict(hdr, pdictlist, f):
    f.writelines(hdr + "\n")

    dict_str = pprint.pformat(pdictlist)
    f.writelines(f"\t {dict_str}\n")

    f.write("\n")


def intersection(lst1, lst2):
    return list(set(lst1) & set(lst2))


def readSrcLines(content, start, end):
    # print(content[end])
    txt = "".join(content[start - 1:end])
    # print(txt)
    # ftxt = re.split('=(\s)function',txt,1)
    # print(ftxt)

    # functxt = "function"+ txt.split("= function")[1]
    functxt = re.split('= *function', txt, 1)
    if (len(functxt) == 2):
        functxt = "function" + functxt[1]

    lines = functxt.split("\n")
    txt = [line for line in lines if line.strip() != ""]
    functxt = "\n".join(txt)
    if (functxt[-1] == ','):
        functxt = functxt[:-1]

    return functxt


# Run cyclocomp on extracted function text
def getComplexity(string):
    qstr = r'\".*\"'  # remove quoted strings
    string = re.sub(qstr, '', string)

    string = "\"" + string + "\""

    cmdstr = "library(cyclocomp)" + "\n" + "cyclocomp(" + string + ")"
    cmd = ["Rscript", "-e", cmdstr]
    result = subprocess.run(cmd, capture_output=True, encoding='UTF-8')

    if (result.stdout.rstrip() != ""):
        routput = result.stdout.split(' ')[1]
    else:
        routput = 0
    return (int(routput))


def getCohesionMetrics(classdef):
    result = {}

    for i in range(len(classdef)):
        m = 0
        a = 0
        mA = 0

        cname = list(classdef[i].keys())[0]

        val = classdef[i][cname]

        for k in val.keys():
            if (k in ["inherit", "objects"]):
                continue

            mem = val[k]
            m = m + len(mem["methodaccess"].keys())
            a = a + len(mem["access"].keys())

            d = mem["access"]

            for j in d.keys():
                mA = mA + len(d[j])

        if (m > 1) and (a > 0):
            lcom = (m - (mA / a)) / (m - 1)
        else:
            lcom = 0

        result[cname] = lcom

    if (len(result)):
        ave = sum(result.values()) / len(result)
    else:
        ave = 0
    return result, ave


def getCouplingMetrics(classdef):
    # print(classdef)
    result = {}
    resultave = {}

    ca = {}
    ce = {}

    cbo = {}
    for i in range(len(classdef)):
        cname = list(classdef[i].keys())[0]
        val = classdef[i][cname]

        ca[cname] = 0
        ce[cname] = 0

        cbo[cname] = []

        if ("inherit" in val.keys()):

            ca[cname] = len(val["inherit"])
            ce[cname] = 0
            # update the ce for the CE field for the superclass

            for base in val["inherit"]:
                if (base in ce.keys()):
                    ce[base] = ce[base] + 1
                else:
                    ce[base] = 1

                cbo[cname].append(base)
                if (base in cbo.keys()):
                    cbo[base].append(cname)
                else:
                    cbo[base] = []

        if ("objects" in val.keys()):

            ca[cname] = ca[cname] + len(val["objects"])
            # update the ce for the CE field for the superclass

            for base in val["objects"]:
                if (base in ce.keys()):
                    ce[base] = ce[base] + 1
                else:
                    ce[base] = 1

                cbo[cname].append(base)
                if (base in cbo.keys()):
                    cbo[base].append(cname)
                else:
                    cbo[base] = 0

        else:
            ca[cname] = 0
            ce[cname] = 0

    mi = {}

    for k in ce.keys():
        if ce[k] + ca[k]:
            mi[k] = ca[k] / (ca[k] + ce[k])
        else:
            mi[k] = 0

        cbo[k] = len(cbo[k])
        data = {"CA": ca[k], "CE": ce[k], "CBO": cbo[k], "MI": mi[k]}
        result[k] = data

    '''
    result["CA"]=ca
    result["CE"]=ce
    result["MI"]= mi
    result["CBO"] = cbo
    '''

    if len(ca):
        resultave["CA"] = sum(ca.values()) / len(ca)
        resultave["CE"] = sum(ce.values()) / len(ce)
        resultave["MI"] = sum(mi.values()) / len(mi)
        resultave["CBO"] = sum(cbo.values()) / len(cbo)
    else:
        resultave["CA"] = 0
        resultave["CE"] = 0
        resultave["MI"] = 0
        resultave["CBO"] = 0

        # print(f"CBO {cbo}")
    return result, resultave


def processInterClass(cdef):
    prmetrics = {}
    result = []
    cm, cmave = getCouplingMetrics(cdef)
    coh, ave = getCohesionMetrics(cdef)

    for k in cm.keys():
        d = {}
        d["className"] = k

        d["CA"] = cm[k]["CA"]
        d["CE"] = cm[k]["CE"]
        d["MI"] = cm[k]["MI"]
        d["CBO"] = cm[k]["CBO"]
        d["LCOM"] = coh[k]

        result.append(d)

    # Cohesion CBO,LCOM, CAM
    for k in cmave.keys():
        prmetrics[k] = cmave[k]
    prmetrics["LCOM"] = ave

    return prmetrics, result  # return project level and class level metrics


def processxml(filename, actualfilename=""):
    with open(actualfilename) as f:
        content = f.readlines()
    metrics = {}

    if (actualfilename):
        fname = actualfilename
        basefile = getbasefile(actualfilename)
    else:
        fname = filename
        basefile = getbasefile(filename)

    basefile = getbasefile(fname)

    # print("-"*50)
    tree = ET.parse(filename)
    root = tree.getroot()

    loc = getNumLOC(root)

    fcalls = getFunctionCalls(root)
    fdef = getFunctionDefinitions(root)

    ifuncs = intersection(fdef, fcalls)

    numcalls = len(fcalls)
    internalcalls = len(ifuncs)
    externalcalls = (numcalls - internalcalls)

    lpkg = getLibPkg(root)

    cdef, count = getClassDef(root)

    # getS4ClassDef(root)

    total_pub_fields = 0
    total_pri_fields = 0

    total_pub_methods = 0
    total_pri_methods = 0

    dam = 0

    totalcomplexity = 0
    avecomplexity = 0
    methodcallcount = 0

    # print(cdef)
    # print(count)
    for i in range(len(cdef)):
        cname = list(cdef[i].keys())[0]

        # print(count[i])
        val = count[i][cname]
        # print(val)

        # print(cdef[i])
        if ("public" in val.keys()):
            pnum = val["public"]
            total_pub_fields = total_pub_fields + pnum[0]
            total_pub_methods = total_pub_methods + pnum[1]

        if ("private" in val.keys()):
            pnum = val["private"]
            total_pri_fields = total_pri_fields + pnum[0]
            total_pri_methods = total_pri_methods + pnum[1]

        if (total_pri_fields + total_pub_fields):
            dam = total_pri_fields / (total_pri_fields + total_pub_fields)

        valdict = (list(cdef[i].values())[0])

        methodcallcount = 0
        totalcomplexity = 0
        methodcount = 0
        for item in valdict.values():

            if (isinstance(item, dict)):
                methodcalls = item["methodcalls"]
                methodcallcount += len(methodcalls)
                funclist = item["methods"]
                for func in funclist:
                    funcname, line1, line2 = func
                    start = int(line1)
                    end = int(line2)

                    functxt = readSrcLines(content, start, end)
                    c = getComplexity(functxt)

                    totalcomplexity = totalcomplexity + c
                    methodcount = methodcount + 1

        avecomplexity = (totalcomplexity / methodcount)

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
    metrics["AMC"] = int(avecomplexity)
    metrics["WMC"] = totalcomplexity
    metrics["RFC"] = methodcallcount

    return cdef, metrics


def main():
    result = {}

    metrics = []
    result["ScriptVersion"] = ScriptVersion

    classdef = []

    if __name__ == "__main__":
        # print(f"Arguments count: {len(sys.argv)}")
        for i, arg in enumerate(sys.argv):
            if (i > 0):
                rscript = ["Rscript", "RParse.R", arg, "temp.xml"]
                print(f"Processing file {arg}")
                # print(rscript)
                cmd = subprocess.run(rscript, capture_output=True, encoding='UTF-8')

                if (cmd.returncode):
                    print(f"Parser Error in {arg}")
                    print(cmd.stderr)
                else:
                    cdef, filemetric = processxml("temp.xml", arg)
                    metrics.append(filemetric)
                    classdef.extend(cdef)

    prmetrics, classmetrics = processInterClass(classdef)

    result["metrics"] = metrics
    result["projectmetrics"] = prmetrics
    result["classmetrics"] = classmetrics

    print("Generating  file metrics.json ...")
    with open('metrics.json', 'w') as fp:
        json.dump(result, fp)


main()
