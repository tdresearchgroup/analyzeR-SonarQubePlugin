
#
# Generates  R PARSE Tree in XML (using in built in Parser)
# First Step in the R metrics generation 

#  TO DO 
# - Add Debug/ logs in separate file ?
# 
# Check if file exists 
# check for other error conditions 


library(xmlparsedata)
getxml <- function(srcfile,dstfile) {
    txt<-readLines(srcfile)
    print(paste("Parsing R file ",srcfile))
    p_txt<-parse(text=txt,,keep.source = TRUE)
    xml_text <- xmlparsedata::xml_parse_data(p_txt, pretty = TRUE)
    #print(paste("Writing XML file ",dstfile))
    writeLines(xml_text,dstfile)
}


args = commandArgs(trailingOnly=TRUE)
if (length(args) !=2) {
  stop("Rscript getxml.R <src.R> <dst.xml>", call.=FALSE)
} else  {
  # default output file
  src<-args[1]
  dst<-args[2]

}
#getxml("src/s3class1.R","xml/s3class1.xml")
getxml(src,dst)