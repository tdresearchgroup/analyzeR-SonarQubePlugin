library(xmlparsedata)

#' Generates a R Parse Tree as an XML R PARSE Tree in XML using \code{xmlparsedata}
#' Reads R source files, and saves it to an XML source file.
#'
#' @importFrom xmlparsedata xml_parse_data
#' 
#' @param srcfile The R file absolute URL. It will be read by line.
#' @param dstfile The destination XML file, where the parse tree will be stored.
#' 
#' @author Pranav Chandramouli, University of Saskatchewan.
#'         Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
#' @return None
#' 
#' @example
#' getxml(example.R,example.xml)
#' 
getxml <- function(srcfile, dstfile) {
    txt<-readLines(srcfile)
    print(paste("Parsing R file ",srcfile))
    p_txt<-parse(text=txt,,keep.source = TRUE)
    xml_text <- xmlparsedata::xml_parse_data(p_txt, pretty = TRUE)
    #print(paste("Writing XML file ",dstfile))
    writeLines(xml_text,dstfile)
}

# Read command line arguments
args = commandArgs(trailingOnly=TRUE)


# Check for invalid command line arguments, and if not, call getxml
if (length(args) !=2) {
  stop("Rscript getxml.R <src.R> <dst.xml>", call.=FALSE)
} else  {
  # default output file
  src<-args[1]
  dst<-args[2]
}


getxml(src,dst)