#' Data extraction of the ballgown results to visualize with \code{viewGene}
#'
#' \code{infoGene} extracts the information according to a geneID. This can be the rcounts or mrcounts per exon (and introns optionall). Alternatively, it can extract the raw coverage.
#'
#' @param geneID specifies the geneID to look at
#' @param gown specifies the output from \code{readGown}
#' @param group specifies the sample groups and will be used to determine the colors.
#' @param countIntron specifies whether to use the intron data or not. If set to TRUE, \code{whichCount} has to be mrcount, rcount or ucount.
#' @param whichCount has to be mrcount, rcount or ucount if \code{countIntron=TRUE}. Otherwise it can also be cov and mcov.
#' @return A list with the information needed to make the plot with \code{viewGene}
#' @export
#' @author Leonardo Collado-Torres \email{lcollado@@jhsph.edu}
#' @examples
#' dataDir <- system.file("extdata", "ballgownData", package="ballgownR")
#' samplePattern <- "sample"
#' gown <- readGown(dataDir=dataDir, samplePattern=samplePattern)
#' info <- system.file("extdata", "sample_info.txt", package="ballgownR")
#' info <- read.table(info, header=TRUE)
#' match <- sapply(names(gown$dirs), function(x) { which(info$dir == x)})
#' geneInfo <- infoGene(geneID="gene_1", gown=gown, group=info$outcome[match])
#' @seealso viewGene

infoGene <- function(geneID, gown, group, countIntron=TRUE, whichCount="mrcount") {
	## Find region of interest
	idx.t <- which(gown$trans$gene_id == geneID)
	
	## Find start and end
	start <- min(gown$trans$start[idx.t])
	end <- max(gown$trans$end[idx.t])	
	nBases <- end - start + 1
	
	## Subset trans, exon and intron
	t.id <- gown$trans$t_id[idx.t]
	names(t.id) <- gown$trans$t_name[idx.t]
	e.id <- lapply(t.id, function(x) { gown$e2t$e_id[ gown$e2t$t_id == x ] })
	
	## Get sample names and the exon/intron rcount columns
	count.e <- which(gsub("\\..*", "", names(gown$exon[e.id[[1]][1], ])) == whichCount)
	samples <- gsub(paste0(whichCount, "\\."), "", names(gown$exon[e.id[[1]][1], ]))[count.e]
	
	## Get exon information by transcript
	exons.df <- lapply(1:length(e.id), function(e) {	
		## Get transcript name
		line <- names(e.id)[[e]]		
		res <- lapply(e.id[[e]], function(exon) {	
			x <- c(gown$exon$start[exon]-1, gown$exon$start[exon], gown$exon$end[exon], gown$exon$end[exon]+1)			
			y <- c(0, e, e, 0)
			data.frame(line=rep(line, length(x)), x=x, y=y)				 
		})
		do.call(rbind, res)
	})
	exons.df <- do.call(rbind, exons.df)
	
	## Build data: either summarized data 
				
	## Initialize rcount data
	count.df <- data.frame(line=rep(samples, each=nBases), x=start:end, y=rep(NA, each=nBases))
	
	## Count exon information
	count.df <- .countInfo(ids=e.id, df=gown$exon, whichCols=count.e, result=count.df)
	
	## Count intron information
	if(countIntron & whichCount %in% c("ucount", "rcount", "mrcount")) {
		i.id <- lapply(t.id, function(x) { gown$i2t$i_id[ gown$i2t$t_id == x ] })
		count.i <- which(gsub("\\..*", "", names(gown$intron[i.id[[1]][1], ])) == whichCount)
		count.df <- .countInfo(ids=i.id, df=gown$intron, whichCols=count.i, result=count.df)
	}
	
	## Finish
	toAdd <- count.df

	## Purge useless info
	toAdd.filt <- .filterInfoGene(toAdd)
	
	## Merge data
	data <- list(exons=exons.df, transInfo=toAdd.filt, start=start, end=end, group=group, geneID=geneID, ylab=whichCount)
	
	## Done
	return(data)
	
}

## This function counts the exon/intron information
.countInfo <- function(ids, df, whichCols, result) {
	for(id in 1:length(ids)) {
		for(xon in ids[[id]]) {
			x <- seq(df$start[xon], df$end[xon])			
			vals <- as.vector(as.matrix(df[xon, whichCols]))
			y <- rep(vals, each=length(x))
			idx <- result$x %in% x
			result$y[idx] <- pmax(result$y[idx],  y, na.rm=TRUE)
		}
	}
	return(result)
}

## This function removes uninformative points to avoid overloading the html
.filterInfoGene <- function(data) {
	## The filtering is done by unique elements of data$line (for example, by sample)
	result <- lapply(unique(data$line), function(x) {
		sub <- data[ data$line == x, ]
		sub$y[is.na(sub$y)] <- 0
		which.diff <- which(diff(sub$y) != 0)
		if(length(which.diff) == 0) {
			res <- rbind(sub[1, ], sub[nrow(sub), ])
		} else {
			res <- rbind(sub[1, ], sub[as.vector(sapply(which.diff, function(z) { c(z, z+1)})), ])
			res <- rbind(res, sub[nrow(sub), ])
		}
		return(res)
	}) 
	result <- do.call(rbind, result)
	rownames(result) <- 1:nrow(result)
	return(result)
}