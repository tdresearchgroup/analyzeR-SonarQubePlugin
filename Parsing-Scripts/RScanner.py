import json
import pprint
import re
import subprocess
import sys
import xml.etree.ElementTree as ET
from copy import deepcopy
from lxml import etree

"""
Script Version, might be useful for debugging.
"""
SCRIPT_VERSION = "1.0"
"""
List of R6 System keyboards
"""
R6_KEYWORDS = ['R6Class', 'if_any', 'map', 'reset', 'new']
"""
List of S4 System Keywords
"""
S4_KEYWORDS = ['setMethod', 'slot', 'setClass', 'representation', 'selectMethod', 'setGeneric', 'signature']



def print_xml_path(fname):
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


def get_built_ins():
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


builtins = get_built_ins()


def get_num_comments(root):
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


def get_num_loc(root):
    """
    Gets Number of Lines of code
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

def get_function_calls(root):
    """
    Gets a List of function calls
    :param root: Root node of an XML Element tree
    :type root: XML ElementTree root node
    :return: Function Calls
    :rtype: List
    """
    result = []
    for c in root.findall('.//SYMBOL_FUNCTION_CALL'):
        if (c.text in builtins) or (c.text in R6_KEYWORDS) or (c.text in S4_KEYWORDS):
            pass
        else:
            result.append(c.text)
    return list(set(result))


def get_function_definitions(root):
    """
    Gets a list of function definitions
    :param root: Root node of an XML Element tree
    :type root: XML ElementTree root node
    :return: Function Definitions
    :rtype: List
    """
    result = []
    for gp in root.findall('.//FUNCTION/../..'):

        etags, etxt, elist = get_child_list(gp)
        if etags == ['expr', 'LEFT_ASSIGN', 'expr']:
            lhs = elist[0]
            sym = find_child(lhs, 'SYMBOL')
            result.append(sym)
    return list(set(result))


def get_library_packages(root):
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
        etags, etxt, elist = get_child_list(item)
        if etags == ['expr', 'OP-LEFT-PAREN', 'expr', 'OP-RIGHT-PAREN']:
            sym = find_child(elist[2], 'SYMBOL')
            result.append(sym)

    return list(set(result))


def find_sublist_index(sublist, patterns_list):
    """
    find the list indices of functions matching the list of patterns
    :param sublist: SubList
    :type sublist: List
    :param patterns_list: List of Patterns
    :type patterns_list: List
    :return: Tuple positions
    :rtype: List
    """
    results = []
    sl_len = len(sublist)
    for ind in (i for i, e in enumerate(patterns_list) if e == sublist[0]):
        if patterns_list[ind:ind + sl_len] == sublist:
            results.append((ind, ind + sl_len - 1))

    # return a list of tuple positions 
    return results


def get_child_list(item):
    """
    Get a list of an item's children
    :param item: Node to be queried.
    :type item: Node
    :return: Elements, Tag, and Text
    :rtype: List, List, List
    """
    elist = list(item.iterfind('*'))
    etags = [elem.tag for elem in elist]
    etext = [elem.text for elem in elist]

    return etags, etext, elist


def find_child(item, tag):
    """
    find a child with specific tag
    :param item: Node to be queried.
    :type item: Node
    :param tag: Tag to be queried
    :type tag: String
    :return:
    :rtype:
    """
    result = list(item.iterfind(tag))
    if len(result):
        return result[0].text
    else:
        return ""


def find_children_with_tag(item, tag):
    """
    find children with specific tag
    :param item: Node to be queried.
    :type item: Node
    :param tag: Tag to be queried
    :type tag: String
    :return: List of children with the tag
    :rtype: List
    """
    result = list(item.iterfind(tag))
    for i in item.findall(tag):
        ET.dump(i)
    lst = []
    if len(result):
        for i in range(len(result)):
            lst.append(result[i].text)

        return lst
    else:
        return ""


def find_grand_children(item, tag):
    """
    find grandchildren with specific tag
    :param item: Node to be queried.
    :type item: Node
    :param tag: Tag to be queried
    :type tag: String
    :return: List of children with the tag
    :rtype: List
    """
    children = list(item.iterfind('*'))
    result = []
    for item in children:
        txt = find_child(item, tag)
        if txt != "":
            result.append(txt)
    return result


def find_child_item(item, tag):
    """
    find the children matching tag , return first item
    :param item: Node to be queried.
    :type item: Node
    :param tag: Tag to be queried
    :type tag: String
    :return: List of children with the tag
    :rtype: List
    """
    result = list(item.iterfind(tag))
    if len(result):
        return result[0]
    else:
        return ""


def find_children(item, tag):
    """
    find the children matching tag , return list
    :param item: Node to be queried.
    :type item: Node
    :param tag: Tag to be queried
    :type tag: String
    :return: List of children with the tag
    :rtype: List
    """
    result = list(item.iterfind(tag))
    if (len(result)):
        return (result)
    else:
        return []


def get_call_list(itemlist):
    """
    Get a list of calls. This is used for getting a list of function calls, from an item.
    :param itemlist: items whose calls must be returned.
    :type itemlist: List
    :return: List of calls
    :rtype: List
    """
    calllist = []
    for item in itemlist:
        calls = get_function_calls(item)
        calllist.extend(calls)

    return calllist


def get_access_list(itemlist):
    """
    gets the list of objects(class members) accessed by a function
    This is used for the coupling metrics
    :param itemlist: items
    :type itemlist: List
    :return: List of objects accessed by a function
    :rtype: List
    """
    result = []
    i = 0
    for item in itemlist:

        for id in item.findall('.//expr'):
            etags, etxt, elist = get_child_list(id)
            indxs = find_sublist_index(['OP-DOLLAR', 'SYMBOL'], etags)
            for i in range(len(indxs)):
                a, b = indxs[i]
                result.append(etxt[a + 1])

    return list(set(result))


def sublist(lst1, lst2):
    ls1 = [element for element in lst1 if element in lst2]
    ls2 = [element for element in lst2 if element in lst1]
    return ls1 == ls2


def get_object_fields(item):
    """
    Find All members which are object type by checking the new
    :param item: Node to be queried.
    :type item: Node
    :return: List of objects
    :rtype: List
    """
    objlist = []

    # find if a new Object/class is created 
    for it in item.findall('./expr/expr/expr/SYMBOL_FUNCTION_CALL[.=\'new\']/..'):
        obj = it.find('./expr/SYMBOL').text
        objlist.append(obj)

    return objlist


def get_r6_class_definitions(item):
    """
    Returns a list of class definitions of R6 class Type
    :param item: Node to be queried.
    :type item: Node
    :return: list of class definitions
    :rtype: List
    """
    result = {}
    result_count = {}

    etags, etxt, elist = get_child_list(item)
    if (etags == ['expr', 'LEFT_ASSIGN', 'expr']):
        lhs = elist[0]
        rhs = elist[2]

        sym = find_child(lhs, 'SYMBOL')
        # print(sym)

        etags, etxt, elist = get_child_list(rhs)

    members = {}
    accessors = {}
    methodacc = {}
    count = {}  # dictionary to hold the count of methods , fields
    indxs = find_sublist_index(['SYMBOL_SUB', 'EQ_SUB', 'expr'], etags)
    # print(indxs)
    for i in range(len(indxs)):
        inhlist = []

        a, b = indxs[i]
        mtype = etxt[a]  # public , private or active declarations

        if (mtype == 'inherit'):
            subclass = find_child(elist[b], 'SYMBOL')
            inhlist.append(subclass)

        tag, txt, el = get_child_list(elist[b])
        method_cnt = 0
        field_cnt = 0

        memdict = {}
        memfunc = []
        memflds = []

        calllist = []
        objfields = []

        i2 = find_sublist_index(['SYMBOL_SUB', 'EQ_SUB', 'expr'], tag)
        for i in range(len(i2)):
            a, b = i2[i]
            line1 = el[b].get("line1")
            line2 = el[b].get("line2")
            tag3, txt3, el3 = get_child_list(el[b])
            if (tag3[0] == 'FUNCTION'):
                fname = txt[a]
                # print(f"Method    :  {fname} {line1} {line2}")
                calllist.append(fname)
                memfunc.append((fname, line1, line2))
                method_cnt = method_cnt + 1
                calls = get_call_list(el3)
                alist = get_access_list(el3)
                methodacc[fname] = alist
                calllist.extend(calls)

                for a in alist:
                    if a in accessors.keys():
                        accessors[a].append(fname)

                if (fname == "initialize"):
                    for j in range(len(el3)):
                        # ET.dump(el3[j])
                        objfields.extend(get_object_fields(el3[j]))
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

    # Returns two dictionaries

    return result


def get_s4_function_details(carray, item):
    """
    Gets function details for S4 Class Functions
    Details are appended to carray
    :param item: Node to be queried.
    :type item: Node
    :return: None
    """
    found = False
    etags, etxt, elist = get_child_list(item)

    idx = find_sublist_index(['OP-LEFT-PAREN', 'expr', 'OP-COMMA', 'expr', 'OP-COMMA', 'expr'], etags)
    idx2 = find_sublist_index(
        ['OP-LEFT-PAREN', 'expr', 'OP-COMMA', 'SYMBOL_SUB', 'EQ_SUB', 'expr', 'OP-COMMA', 'SYMBOL_SUB', 'EQ_SUB',
         'expr'], etags)

    if (len(idx)):
        a, b = idx[0]
        sym = find_child(elist[a + 1], 'STR_CONST')
        if (sym != ''):
            func = get_string_without_quotes(sym)
        symlst = find_grand_children(elist[a + 3], 'STR_CONST')
        if (len(symlst)):
            cname = get_string_without_quotes(symlst[0])

        line1 = elist[b].get("line1")
        line2 = elist[b].get("line2")
        tag3, txt3, el3 = get_child_list(elist[b])
        if (tag3[0] == 'FUNCTION'):
            found = True
    elif len(idx2):
        a, b = idx2[0]
        sym = find_child(elist[a + 1], 'STR_CONST')
        if (sym != ''):
            func = get_string_without_quotes(sym)
        symlst = find_grand_children(elist[a + 5], 'STR_CONST')
        if (len(symlst)):
            cname = get_string_without_quotes(symlst[0])

        line1 = elist[b].get("line1")
        line2 = elist[b].get("line2")
        tag3, txt3, el3 = get_child_list(elist[b])
        if (tag3[0] == 'FUNCTION'):
            found = True

    if (found):
        for i in range(len(carray)):
            if (cname in carray[i].keys()):
                carray[i][cname]["public"]["methods"].append((func, line1, line2))


def get_s4_class_definitions(root):
    """
    Gets a list of S4 Class definitions
    :param root: Node Being Queried
    :type root: Node
    :return:  a list of class definitions of S4 type
    :rtype: List
    """
    result = []

    classfields = {}
    count = {}  # dictionary to hold the count of methods , fields

    memdict = {}
    methods = []
    members = []
    inherits = []
    tlst = ['expr', 'OP-LEFT-PAREN', 'expr']

    classlist = []

    # get the grandparent expr of SYMBOL_FUNCTION_CALL
    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'setClass\']/../..'):
        etags, etxt, elist = get_child_list(item)

        memdict = {}
        dct = {}
        members = []
        inherits = []
        objfields = []
        idx = find_sublist_index(tlst, etags)

        memdict["methods"] = []
        memdict["fields"] = []
        memdict["methodcalls"] = []
        memdict["access"] = {}  # this dictionary has fields to method mapping
        memdict["methodaccess"] = {}  # this dictionary has method to fields access

        classfields["inherit"] = []
        classfields["objects"] = []
        if (len(idx)):
            a, b = idx[0]
            sym = find_child(elist[b], 'STR_CONST')
            classname = get_string_without_quotes(sym)
            classlist.append(classname)

            rep = item.find('.//SYMBOL_FUNCTION_CALL[.=\'representation\']/../..')
            if (rep):
                etags2, etxt2, elist2 = get_child_list(rep)

                idx = find_sublist_index(['SYMBOL_SUB', 'EQ_SUB', 'expr'], etags2)
                for i in range(len(idx)):
                    a, b = idx[i]
                    members.append(etxt2[a])

                    objtype = find_child(elist2[b], 'STR_CONST')
                    objtype = get_string_without_quotes(objtype)

                    if objtype in classlist:
                        if objtype not in objfields:
                            objfields.append(objtype)

            rep = item.find('.//SYMBOL_SUB[.=\'slots\']/..')
            if (rep):

                etags2, etxt2, elist2 = get_child_list(rep)

                idx = find_sublist_index(['SYMBOL_SUB', 'EQ_SUB', 'expr'], etags2)
                for i in range(len(idx)):
                    a, b = idx[i]
                    slots = find_children(elist2[b], 'SYMBOL_SUB')
                    for slot in slots:
                        members.append(slot.text)

            idx = find_sublist_index(['SYMBOL_SUB', 'EQ_SUB', 'expr'], etags)
            for i in range(len(idx)):
                a, b = idx[i]
                sym = find_child(elist[b], 'STR_CONST')
                if (sym != ''):
                    inherits.append(get_string_without_quotes(sym))

            memdict["fields"].extend(members)

            classfields["public"] = memdict
            classfields["inherit"].extend(inherits)
            classfields["objects"].extend(objfields)

            dct[classname] = deepcopy(classfields)

            result.append(deepcopy(dct))

    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'setMethod\']/../..'):
        get_s4_function_details(result, item)

    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'setReplaceMethod\']/../..'):
        get_s4_function_details(result, item)

    return result


def get_class_definitions(root):
    """
    get the class definitions
    Calls s4 and r6 processsing functions
    :param root: Node Being Queried
    :type root: Node
    :return: List of class definitions
    :rtype:
    """
    result = []
    # get the grandparent expr of SYMBOL_FUNCTION_CALL
    for item in root.findall('.//SYMBOL_FUNCTION_CALL[.=\'R6Class\']/../../..'):
        f = get_r6_class_definitions(item)
        result.append(f)

    r = get_s4_class_definitions(root)
    if (len(r)):
        result.extend(r)
    # Return the list of class definitions
    return result


def get_string_without_quotes(str):
    """
    Helper function to return a string without the quotes
    :param str: Input String
    :type str: String
    :return: String with quotes replaced
    :rtype: String
    """
    return str.replace('"', '')


def get_base_file(filepath):
    """
    Returns the Base file from a filepath
    """
    filepath = filepath.split(".")[0]
    flist = filepath.split("/")
    if (len(flist) == 2):
        return flist[1]
    else:
        return flist[0]


def print_list(hdr, plist, f):
    """
    Helper/Debugging function to print a list in an aesthetic way.
    """
    f.writelines(hdr + "\n")
    for i in range(len(plist)):
        f.writelines(f"\t {plist[i]}\n")
    f.write("\n")


def print_dict(hdr, pdictlist, f):
    """
    Helper/Debugging function to print a dictionary in an aesthetic way.
    """
    f.writelines(hdr + "\n")

    dict_str = pprint.pformat(pdictlist)
    f.writelines(f"\t {dict_str}\n")

    f.write("\n")


def intersection(lst1, lst2):
    """
    Returns the intersection of two lists.
    """
    return list(set(lst1) & set(lst2))


def read_source_lines(content, start, end):
    """
    Reads Lines from source and returns the lines as a string
    :param content: Content of the Source File
    :type content: String
    :param start: Start Index
    :type start: Index
    :param end: End Index
    :type end: Index
    :return: function text
    :rtype: String
    """

    txt = "".join(content[start - 1:end])

    functxt = re.split('= *function', txt, 1)
    if (len(functxt) == 2):
        functxt = "function" + functxt[1]

    lines = functxt.split("\n")
    txt = [line for line in lines if line.strip() != ""]
    functxt = "\n".join(txt)
    if (functxt[-1] == ','):
        functxt = functxt[:-1]

    return functxt


def calc_cyclocomp(function_text):
    """
    Runs cyclocomp on extracted function text.
    :param function_text: Function Text
    :type function_text: String
    :return: Cyclomatic Complexity of the text
    :rtype: Int
    """
    qstr = r'\".*\"'  # remove quoted strings
    string = re.sub(qstr, '', function_text)

    string = "\"" + string + "\""

    cmdstr = "library(cyclocomp)" + "\n" + "cyclocomp(" + string + ")"
    cmd = ["Rscript", "-e", cmdstr]
    result = subprocess.run(cmd, capture_output=True, encoding='UTF-8')

    if (result.stdout.rstrip() != ""):
        routput = result.stdout.split(' ')[1]
    else:
        routput = 0
    return (int(routput))


def get_cohesion_metrics(classdef):
    """
    Calculates cohesion metrics for classdefinitions
    :param classdef: the class definition in question
    :type classdef:
    :return: LCOM dictionary and the average
    :rtype: Dictionary, Float
    """
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


def get_coupling_metrics(classdef):
    """
    Gets Coupling Metrics for a class definition
    :param classdef: the class definition in question
    :type classdef:
    :return: Coupling Metrics and Average Coupling Metrics
    :rtype: Dictionary, Dictionary
    """
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
        if (k not in ca.keys()):
            ca[k] = 0
        if ce[k] + ca[k]:
            mi[k] = ca[k] / (ca[k] + ce[k])
        else:
            mi[k] = 0

        if (k in cbo.keys()):
            #print(cbo[k])
            if (isinstance(cbo[k],list)):
                cbo[k] = len(cbo[k])
        else:
            cbo[k] = 0
        data = {"CA": ca[k], "CE": ce[k], "CBO": cbo[k], "MI": mi[k]}
        result[k] = data

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


def process_interclass_metrics(cdef):
    """
    Use previously defined functions to process interclass metrics - project level and class-level
    :param cdef: Class definitions
    :type cdef:
    :return:  project level and class level metrics
    """
    prmetrics = {}
    result = []
    cm, cmave = get_coupling_metrics(cdef)
    coh, ave = get_cohesion_metrics(cdef)

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

    return prmetrics, result


def processxml(filename, actualfilename=""):
    """
    Processes an XML file, using other functions
    :param filename: XML Filename
    :type filename: String
    :param actualfilename:
    :type actualfilename: String
    :return: ClassDefinitions and Metrics
    :rtype: List, Dictionary
    """
    with open(actualfilename) as f:
        content = f.readlines()
    metrics = {}

    if (actualfilename):
        fname = actualfilename
        basefile = get_base_file(actualfilename)
    else:
        fname = filename
        basefile = get_base_file(filename)

    basefile = get_base_file(fname)

    # print("-"*50)
    tree = ET.parse(filename)
    root = tree.getroot()

    get_s4_class_definitions(root)

    loc = get_num_loc(root)

    fcalls = get_function_calls(root)
    fdef = get_function_definitions(root)

    ifuncs = intersection(fdef, fcalls)

    numcalls = len(fcalls)
    internalcalls = len(ifuncs)
    externalcalls = (numcalls - internalcalls)

    lpkg = get_library_packages(root)

    cdef = get_class_definitions(root)

    # get_s4_class_definitions(root)

    total_pub_fields = 0
    total_pri_fields = 0

    total_pub_methods = 0
    total_pri_methods = 0

    dam = 0

    totalcomplexity = 0
    avecomplexity = 0
    methodcallcount = 0

    # print(cdef)

    for i in range(len(cdef)):
        cname = list(cdef[i].keys())[0]
        val = cdef[i][cname]

        if ("public" in val.keys()):
            total_pub_fields = total_pub_fields + len(val["public"]["fields"])
            total_pub_methods = total_pub_methods + len(val["public"]["methods"])
        if ("private" in val.keys()):
            total_pri_fields = total_pri_fields + len(val["private"]["fields"])
            total_pri_methods = total_pri_methods + len(val["private"]["methods"])

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

                    functxt = read_source_lines(content, start, end)
                    c = calc_cyclocomp(functxt)

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
    """
    Main function, that reads commandline arguments, calls the parsing script, and then calls the metrics
    calculations on the parsed xml
    """
    result = {}

    metrics = []
    result["SCRIPT_VERSION"] = SCRIPT_VERSION

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
                    # print_xml_path("temp.xml")
                    metrics.append(filemetric)
                    classdef.extend(cdef)

    prmetrics, classmetrics = process_interclass_metrics(classdef)

    result["metrics"] = metrics
    result["moduleMetrics"] = prmetrics
    result["classmetrics"] = classmetrics

    print("Generating  file metrics.json ...")
    with open('metrics.json', 'w') as fp:
        json.dump(result, fp)


main()
