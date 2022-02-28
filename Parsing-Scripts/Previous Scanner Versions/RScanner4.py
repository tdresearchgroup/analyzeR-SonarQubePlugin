import xml.etree.ElementTree as ET

import subprocess
import sys

from lxml import etree
import pprint
import json
import re

builtins = ['zapsmall', 'xzfile', 'xtfrm.POSIXlt', 'xtfrm.POSIXct', 'xtfrm.numeric_version', 'xtfrm.factor', 'xtfrm.difftime', 'xtfrm.default', 'xtfrm.Date', 'xtfrm.AsIs', 'xtfrm', 'xpdrows.data.frame', 'xor', 'writeLines', 'writeChar', 'writeBin', 'write.dcf', 'write', 'withVisible', 'withRestarts', 'within.list', 'within.data.frame', 'within', 'withCallingHandlers', 'withAutoprint', 'with.default', 'with', 'while', 'which.min', 'which.max', 'which', 'weekdays.POSIXt', 'weekdays.Date', 'weekdays', 'warnings', 'warningCondition', 'warning', 'version', 'Vectorize', 'vector', 'vapply', 'validUTF8', 'validEnc', 'utf8ToInt', 'UseMethod', 'url', 'upper.tri', 'unz', 'untracemem', 'untrace', 'unsplit', 'unserialize', 'unname', 'unlockBinding', 'unloadNamespace', 'unlist', 'unlink', 'unix.time', 'units<-.difftime', 'units<-', 'units.difftime', 'units', 'unique.warnings', 'unique.POSIXlt', 'unique.numeric_version', 'unique.matrix', 'unique.default', 'unique.data.frame', 'unique.array', 'unique', 'union', 'undebug', 'unclass', 'typeof', 'tryCatch', 'try', 'truncate.connection', 'truncate', 'trunc.POSIXt', 'trunc.Date', 'trunc', 'trimws', 'trigamma', 'transform.default', 'transform.data.frame', 'transform', 'tracingState', 'tracemem', 'traceback', 'trace', 'toupper', 'toString.default', 'toString', 'topenv', 'tolower', 'textConnectionValue', 'textConnection', 'tempfile', 'tempdir', 'tcrossprod', 'taskCallbackManager', 'tapply', 'tanpi', 'tanh', 'tan', 'tabulate', 'table', 't.default', 't.data.frame', 'T', 't', 'system2', 'system.time', 'system.file', 'system', 'Sys.which', 'Sys.unsetenv', 'Sys.umask', 'Sys.timezone', 'Sys.time', 'sys.status', 'sys.source', 'Sys.sleep', 'Sys.setlocale', 'Sys.setFileTime', 'Sys.setenv', 'sys.save.image', 'Sys.readlink', 'sys.parents', 'sys.parent', 'sys.on.exit', 'sys.nframe', 'Sys.localeconv', 'sys.load.image', 'Sys.info', 'Sys.glob', 'Sys.getpid', 'Sys.getlocale', 'Sys.getenv', 'sys.function', 'sys.frames', 'sys.frame', 'Sys.Date', 'Sys.chmod', 'sys.calls', 'sys.call', 'switch', 'sweep', 'svd', 'suspendInterrupts', 'suppressWarnings', 'suppressPackageStartupMessages', 'suppressMessages', 'summary.warnings', 'summary.table', 'summary.srcref', 'summary.srcfile', 'summary.proc_time', 'Summary.POSIXlt', 'summary.POSIXlt', 'Summary.POSIXct', 'summary.POSIXct', 'Summary.ordered', 'Summary.numeric_version', 'summary.matrix', 'Summary.factor', 'summary.factor', 'Summary.difftime', 'summary.default', 'Summary.Date', 'summary.Date', 'Summary.data.frame', 'summary.data.frame', 'summary.connection', 'summary', 'sum', 'substring<-', 'substring', 'substr<-', 'substr', 'substitute', 'subset.matrix', 'subset.default', 'subset.data.frame', 'subset', 'sub', 'strwrap', 'structure', 'strtrim', 'strtoi', 'strsplit', 'strrep', 'strptime', 'strftime', 'str2lang', 'str2expression', 'storage.mode<-', 'storage.mode', 'stopifnot', 'stop', 'stdout', 'stdin', 'stderr', 'startsWith', 'standardGeneric', 'srcref', 'srcfilecopy', 'srcfilealias', 'srcfile', 'sQuote', 'sqrt', 'sprintf', 'split<-.default', 'split<-.data.frame', 'split<-', 'split.POSIXct', 'split.default', 'split.Date', 'split.data.frame', 'split', 'source', 'sort.POSIXlt', 'sort.list', 'sort.int', 'sort.default', 'sort', 'solve.qr', 'solve.default', 'solve', 'socketSelect', 'socketConnection', 'slice.index', 'sinpi', 'sink.number', 'sink', 'sinh', 'single', 'sin', 'simplify2array', 'simpleWarning', 'simpleMessage', 'simpleError', 'simpleCondition', 'signif', 'signalCondition', 'sign', 'shQuote', 'showConnections', 'setwd', 'setTimeLimit', 'setSessionTimeLimit', 'setNamespaceInfo', 'setHook', 'setequal', 'setdiff', 'set.seed', 'serialize', 'sequence', 'seq.POSIXt', 'seq.int', 'seq.default', 'seq.Date', 'seq_len', 'seq_along', 'seq', 'seek.connection', 'seek', 'searchpaths', 'search', 'scan', 'scale.default', 'scale', 'saveRDS', 'save.image', 'save', 'sapply', 'sample.int', 'sample', 'rowSums', 'rowsum.default', 'rowsum.data.frame', 'rowsum', 'rownames<-', 'rownames', 'rowMeans', 'row.names<-.default', 'row.names<-.data.frame', 'row.names<-', 'row.names.default', 'row.names.data.frame', 'row.names', 'row', 'round.POSIXt', 'round.Date', 'round', 'RNGversion', 'RNGkind', 'rm', 'rle', 'rev.default', 'rev', 'returnValue', 'return', 'retracemem', 'restartFormals', 'restartDescription', 'requireNamespace', 'require', 'replicate', 'replace', 'repeat', 'rep.POSIXlt', 'rep.POSIXct', 'rep.numeric_version', 'rep.int', 'rep.factor', 'rep.Date', 'rep_len', 'rep', 'removeTaskCallback', 'remove', 'regmatches<-', 'regmatches', 'registerS3methods', 'registerS3method', 'regexpr', 'regexec', 'reg.finalizer', 'Reduce', 'Recall', 'readRenviron', 'readRDS', 'readLines', 'readline', 'readChar', 'readBin', 'read.dcf', 'Re', 'rcond', 'rbind.data.frame', 'rbind', 'rawToChar', 'rawToBits', 'rawShift', 'rawConnectionValue', 'rawConnection', 'raw', 'rapply', 'rank', 'range.default', 'range', 'RLanguageDefinition.version.string', 'RLanguageDefinition.Version', 'RLanguageDefinition.version', 'RLanguageDefinition.home', 'R_system_version', 'quote', 'quit', 'quarters.POSIXt', 'quarters.Date', 'quarters', 'qr.X', 'qr.solve', 'qr.resid', 'qr.RLanguageDefinition', 'qr.qy', 'qr.qty', 'qr.Q', 'qr.fitted', 'qr.default', 'qr.coef', 'qr', 'q', 'pushBackLength', 'pushBack', 'psigamma', 'provideDimnames', 'prop.table', 'prod', 'proc.time', 'prmatrix', 'print.warnings', 'print.table', 'print.summaryDefault', 'print.summary.warnings', 'print.summary.table', 'print.srcref', 'print.srcfile', 'print.simple.list', 'print.rle', 'print.restart', 'print.proc_time', 'print.POSIXlt', 'print.POSIXct', 'print.packageInfo', 'print.octmode', 'print.numeric_version', 'print.noquote', 'print.NativeRoutineList', 'print.listof', 'print.libraryIQR', 'print.hexmode', 'print.function', 'print.factor', 'print.eigen', 'print.DLLRegisteredRoutines', 'print.DLLInfoList', 'print.DLLInfo', 'print.Dlist', 'print.difftime', 'print.default', 'print.Date', 'print.data.frame', 'print.connection', 'print.condition', 'print.by', 'print.AsIs', 'print', 'prettyNum', 'pretty.default', 'pretty', 'Position', 'pos.to.env', 'polyroot', 'pmin.int', 'pmin', 'pmax.int', 'pmax', 'pmatch', 'pipe', 'pi', 'pcre_config', 'path.package', 'path.expand', 'paste0', 'paste', 'parseNamespaceFile', 'parse', 'parent.frame', 'parent.env<-', 'parent.env', 'pairlist', 'packBits', 'packageStartupMessage', 'packageNotFoundError', 'packageHasNamespace', 'packageEvent', 'package_version', 'outer', 'ordered', 'order', 'options', 'Ops.POSIXt', 'Ops.ordered', 'Ops.numeric_version', 'Ops.factor', 'Ops.difftime', 'Ops.Date', 'Ops.data.frame', 'open.srcfilecopy', 'open.srcfilealias', 'open.srcfile', 'open.connection', 'open', 'on.exit', 'OlsonNames', 'oldClass<-', 'oldClass', 'objects', 'nzchar', 'numeric_version', 'numeric', 'nullfile', 'NROW', 'nrow', 'normalizePath', 'norm', 'noquote', 'nlevels', 'ngettext', 'NextMethod', 'next', 'new.env', 'Negate', 'NCOL', 'ncol', 'nchar', 'nargs', 'namespaceImportMethods', 'namespaceImportFrom', 'namespaceImportClasses', 'namespaceImport', 'namespaceExport', 'names<-.POSIXlt', 'names<-', 'names.POSIXlt', 'names', 'mostattributes<-', 'months.POSIXt', 'months.Date', 'months', 'month.name', 'month.abb', 'mode<-', 'mode', 'Mod', 'missing', 'min', 'mget', 'message', 'merge.default', 'merge.data.frame', 'merge', 'memory.profile', 'memDecompress', 'memCompress', 'mem.maxVSize', 'mem.maxNSize', 'mem.limits', 'mean.POSIXlt', 'mean.POSIXct', 'mean.difftime', 'mean.default', 'mean.Date', 'mean', 'max.col', 'max', 'matrix', 'Math.POSIXt', 'Math.factor', 'Math.difftime', 'Math.Date', 'Math.data.frame', 'match.fun', 'match.call', 'match.arg', 'match', 'mat.or.vec', 'margin.table', 'mapply', 'Map', 'makeActiveBinding', 'make.unique', 'make.names', 'ls', 'lower.tri', 'logical', 'logb', 'log2', 'log1p', 'log10', 'log', 'lockEnvironment', 'lockBinding', 'local', 'loadNamespace', 'loadingNamespaceInfo', 'loadedNamespaces', 'load', 'list2env', 'list.files', 'list.dirs', 'list', 'license', 'licence', 'library.dynam.unload', 'library.dynam', 'library', 'libcurlVersion', 'lgamma', 'lfactorial', 'levels<-.factor', 'levels<-', 'levels.default', 'levels', 'LETTERS', 'letters', 'lengths', 'length<-.POSIXlt', 'length<-.POSIXct', 'length<-.factor', 'length<-.difftime', 'length<-.Date', 'length<-', 'length.POSIXlt', 'length', 'lchoose', 'lbeta', 'lazyLoadDBfetch', 'lazyLoadDBexec', 'lazyLoad', 'lapply', 'labels.default', 'labels', 'La.svd', 'La_version', 'La_library', 'l10n_info', 'kronecker', 'kappa.qr', 'kappa.lm', 'kappa.default', 'kappa', 'julian.POSIXt', 'julian.Date', 'julian', 'jitter', 'isTRUE', 'isSymmetric.matrix', 'isSymmetric', 'isSeekable', 'isS4', 'isRestart', 'isOpen', 'ISOdatetime', 'ISOdate', 'isNamespaceLoaded', 'isNamespace', 'isIncomplete', 'isFALSE', 'isdebugged', 'isBaseNamespace', 'isatty', 'is.vector', 'is.unsorted', 'is.table', 'is.symbol', 'is.single', 'is.recursive', 'is.raw', 'is.RLanguageDefinition', 'is.qr', 'is.primitive', 'is.pairlist', 'is.package_version', 'is.ordered', 'is.object', 'is.numeric.POSIXt', 'is.numeric.difftime', 'is.numeric.Date', 'is.numeric_version', 'is.numeric', 'is.null', 'is.nan', 'is.name', 'is.na<-.numeric_version', 'is.na<-.factor', 'is.na<-.default', 'is.na<-', 'is.na.POSIXlt', 'is.na.numeric_version', 'is.na.data.frame', 'is.na', 'is.matrix', 'is.logical', 'is.loaded', 'is.list', 'is.language', 'is.integer', 'is.infinite', 'is.function', 'is.finite', 'is.factor', 'is.expression', 'is.environment', 'is.element', 'is.double', 'is.data.frame', 'is.complex', 'is.character', 'is.call', 'is.atomic', 'is.array', 'invokeRestartInteractively', 'invokeRestart', 'invisible', 'inverse.rle', 'intToUtf8', 'intToBits', 'intersect', 'interactive', 'interaction', 'integer', 'inherits', 'importIntoEnv', 'Im', 'ifelse', 'if', 'identity', 'identical', 'icuSetCollate', 'icuGetCollate', 'iconvlist', 'iconv', 'I', 'gzfile', 'gzcon', 'gsub', 'grouping', 'grepRaw', 'grepl', 'grep', 'gregexpr', 'globalenv', 'gl', 'getwd', 'gettextf', 'gettext', 'getTaskCallbackNames', 'getSrcLines', 'getRversion', 'getOption', 'getNativeSymbolInfo', 'getNamespaceVersion', 'getNamespaceUsers', 'getNamespaceName', 'getNamespaceInfo', 'getNamespaceImports', 'getNamespaceExports', 'getNamespace', 'getLoadedDLLs', 'getHook', 'getExportedValue', 'geterrmessage', 'getElement', 'getDLLRegisteredRoutines.DLLInfo', 'getDLLRegisteredRoutines.character', 'getDLLRegisteredRoutines', 'getConnection', 'getCallingDLLe', 'getCallingDLL', 'getAllConnections', 'get0', 'get', 'gctorture2', 'gctorture', 'gcinfo', 'gc.time', 'gc', 'gamma', 'function', 'forwardsolve', 'formatDL', 'formatC', 'format.summaryDefault', 'format.pval', 'format.POSIXlt', 'format.POSIXct', 'format.packageInfo', 'format.octmode', 'format.numeric_version', 'format.libraryIQR', 'format.info', 'format.hexmode', 'format.factor', 'format.difftime', 'format.default', 'format.Date', 'format.data.frame', 'format.AsIs', 'format', 'formals<-', 'formals', 'forceAndCall', 'force', 'for', 'flush.connection', 'flush', 'floor', 'findRestart', 'findPackageEnv', 'findInterval', 'find.package', 'Find', 'Filter', 'file.symlink', 'file.size', 'file.show', 'file.rename', 'file.remove', 'file.path', 'file.mtime', 'file.mode', 'file.link', 'file.info', 'file.exists', 'file.create', 'file.copy', 'file.choose', 'file.append', 'file.access', 'file', 'fifo', 'factorial', 'factor', 'F', 'extSoftVersion', 'expression', 'expm1', 'expand.grid', 'exp', 'exists', 'evalq', 'eval.parent', 'eval', 'errorCondition', 'environmentName', 'environmentIsLocked', 'environment<-', 'environment', 'env.profile', 'enquote', 'endsWith', 'Encoding<-', 'Encoding', 'encodeString', 'enc2utf8', 'enc2native', 'emptyenv', 'eigen', 'eapply', 'dynGet', 'dyn.unload', 'dyn.load', 'duplicated.warnings', 'duplicated.POSIXlt', 'duplicated.numeric_version', 'duplicated.matrix', 'duplicated.default', 'duplicated.data.frame', 'duplicated.array', 'duplicated', 'dump', 'droplevels.factor', 'droplevels.data.frame', 'droplevels', 'drop', 'dQuote', 'dput', 'double', 'dontCheck', 'do.call', 'dirname', 'dir.exists', 'dir.create', 'dir', 'dimnames<-.data.frame', 'dimnames<-', 'dimnames.data.frame', 'dimnames', 'dim<-', 'dim.data.frame', 'dim', 'digamma', 'difftime', 'diff.POSIXt', 'diff.difftime', 'diff.default', 'diff.Date', 'diff', 'diag<-', 'diag', 'dget', 'determinant.matrix', 'determinant', 'detach', 'det', 'deparse', 'delayedAssign', 'default.stringsAsFactors', 'debugonce', 'debuggingState', 'debug', 'date', 'data.matrix', 'data.frame', 'data.class', 'cut.POSIXt', 'cut.default', 'cut.Date', 'cut', 'curlGetHeaders', 'cumsum', 'cumprod', 'cummin', 'cummax', 'Cstack_info', 'crossprod', 'cospi', 'cosh', 'cos', 'contributors', 'Conj', 'conflicts', 'conflictRules', 'conditionMessage.condition', 'conditionMessage', 'conditionCall.condition', 'conditionCall', 'computeRestarts', 'complex', 'comment<-', 'comment', 'commandArgs', 'colSums', 'colnames<-', 'colnames', 'colMeans', 'col', 'closeAllConnections', 'close.srcfilealias', 'close.srcfile', 'close.connection', 'close', 'clearPushBack', 'class<-', 'class', 'choose', 'chol2inv', 'chol.default', 'chol', 'chkDots', 'check_tzones', 'chartr', 'charToRaw', 'charmatch', 'character', 'char.expand', 'ceiling', 'cbind.data.frame', 'cbind', 'cat', 'casefold', 'capabilities', 'callCC', 'call', 'c.warnings', 'c.POSIXlt', 'c.POSIXct', 'c.numeric_version', 'c.noquote', 'c.difftime', 'c.Date', 'c', 'bzfile', 'by.default', 'by.data.frame', 'by', 'builtins', 'browserText', 'browserSetDebug', 'browserCondition', 'browser', 'break', 'bquote', 'body<-', 'body', 'bitwXor', 'bitwShiftR', 'bitwShiftL', 'bitwOr', 'bitwNot', 'bitwAnd', 'bindtextdomain', 'bindingIsLocked', 'bindingIsActive', 'beta', 'besselY', 'besselK', 'besselJ', 'besselI', 'basename', 'baseenv', 'backsolve', 'autoloader', 'autoload', 'attributes<-', 'attributes', 'attr<-', 'attr.all.equal', 'attr', 'attachNamespace', 'attach', 'atanh', 'atan2', 'atan', 'assign', 'asS4', 'asS3', 'asplit', 'asNamespace', 'asinh', 'asin', 'as.vector.factor', 'as.vector', 'as.table.default', 'as.table', 'as.symbol', 'as.single.default', 'as.single', 'as.raw', 'as.qr', 'as.POSIXlt.POSIXct', 'as.POSIXlt.numeric', 'as.POSIXlt.factor', 'as.POSIXlt.default', 'as.POSIXlt.Date', 'as.POSIXlt.character', 'as.POSIXlt', 'as.POSIXct.POSIXlt', 'as.POSIXct.numeric', 'as.POSIXct.default', 'as.POSIXct.Date', 'as.POSIXct', 'as.pairlist', 'as.package_version', 'as.ordered', 'as.octmode', 'as.numeric_version', 'as.numeric', 'as.null.default', 'as.null', 'as.name', 'as.matrix.POSIXlt', 'as.matrix.noquote', 'as.matrix.default', 'as.matrix.data.frame', 'as.matrix', 'as.logical.factor', 'as.logical', 'as.list.POSIXlt', 'as.list.POSIXct', 'as.list.numeric_version', 'as.list.function', 'as.list.factor', 'as.list.environment', 'as.list.default', 'as.list.Date', 'as.list.data.frame', 'as.list', 'as.integer', 'as.hexmode', 'as.function.default', 'as.function', 'as.factor', 'as.expression.default', 'as.expression', 'as.environment', 'as.double.POSIXlt', 'as.double.difftime', 'as.double', 'as.difftime', 'as.Date.POSIXlt', 'as.Date.POSIXct', 'as.Date.numeric', 'as.Date.factor', 'as.Date.default', 'as.Date.character', 'as.Date', 'as.data.frame.vector', 'as.data.frame.ts', 'as.data.frame.table', 'as.data.frame.raw', 'as.data.frame.POSIXlt', 'as.data.frame.POSIXct', 'as.data.frame.ordered', 'as.data.frame.numeric_version', 'as.data.frame.numeric', 'as.data.frame.noquote', 'as.data.frame.model.matrix', 'as.data.frame.matrix', 'as.data.frame.logical', 'as.data.frame.list', 'as.data.frame.integer', 'as.data.frame.factor', 'as.data.frame.difftime', 'as.data.frame.default', 'as.data.frame.Date', 'as.data.frame.data.frame', 'as.data.frame.complex', 'as.data.frame.character', 'as.data.frame.AsIs', 'as.data.frame.array', 'as.data.frame', 'as.complex', 'as.character.srcref', 'as.character.POSIXt', 'as.character.octmode', 'as.character.numeric_version', 'as.character.hexmode', 'as.character.factor', 'as.character.error', 'as.character.default', 'as.character.Date', 'as.character.condition', 'as.character', 'as.call', 'as.array.default', 'as.array', 'arrayInd', 'array', 'args', 'Arg', 'apply', 'append', 'aperm.table', 'aperm.default', 'aperm', 'anyNA.POSIXlt', 'anyNA.numeric_version', 'anyNA', 'anyDuplicated.matrix', 'anyDuplicated.default', 'anyDuplicated.data.frame', 'anyDuplicated.array', 'anyDuplicated', 'any', 'allowInterrupts', 'all.vars', 'all.names', 'all.equal.raw', 'all.equal.POSIXt', 'all.equal.numeric', 'all.equal.list', 'all.equal.language', 'all.equal.formula', 'all.equal.factor', 'all.equal.envRefClass', 'all.equal.environment', 'all.equal.default', 'all.equal.character', 'all.equal', 'all', 'alist', 'agrepl', 'agrep', 'addTaskCallback', 'addNA', 'acosh', 'acos', 'abs', 'abbreviate', '$<-.data.frame', '$<-', '$.package_version', '$.DLLInfo', '$', '~', '||', '|.octmode', '|.hexmode', '|', '>=', '>', '==', '=', '<=', '<<-', '<-', '<', '+.POSIXt', '+.Date', '+', '^', '%x%', '%o%', '%in%', '%%', '%/%', '%*%', '&&', '&.octmode', '&.hexmode', '&', '/.difftime', '/', '*.difftime', '*', '@<-', '@', '{', '[<-.POSIXlt', '[<-.POSIXct', '[<-.numeric_version', '[<-.factor', '[<-.Date', '[<-.data.frame', '[<-', '[[<-.POSIXlt', '[[<-.numeric_version', '[[<-.factor', '[[<-.data.frame', '[[<-', '[[.POSIXlt', '[[.POSIXct', '[[.numeric_version', '[[.factor', '[[.Date', '[[.data.frame', '[[', '[.warnings', '[.table', '[.simple.list', '[.POSIXlt', '[.POSIXct', '[.octmode', '[.numeric_version', '[.noquote', '[.listof', '[.hexmode', '[.factor', '[.DLLInfoList', '[.Dlist', '[.difftime', '[.Date', '[.data.frame', '[.AsIs', '[', '(', '.valid.factor', '.userHooksEnv', '.tryResumeInterrupt', '.traceback', '.TAOCP1997init', '.subset2', '.subset', '.standard_regexps', '.signalSimpleWarning', '.set_row_names', '.Script', '.saveRDS', '.S3PrimitiveGenerics', '.S3_methods_table', '.rowSums', '.rowNamesDF<-', '.rowMeans', '.row_names_info', '.row', '.rmpkg', '.readRDS', '.primUntrace', '.primTrace', '.Primitive', '.POSIXlt', '.POSIXct', '.popath', '.Platform', '.path.package', '.packageStartupMessage', '.packages', '.OptRequireMethods', '.Options', '.NotYetUsed', '.NotYetImplemented', '.noGenerics', '.methodsNamespace', '.mergeImportMethods', '.mergeExportMethods', '.maskedMsg', '.mapply', '.makeMessage', '.make_numeric_version', '.Machine', '.Library.site', '.Library', '.libPaths', '.leap.seconds', '.Last.value', '.kronecker', '.knownS3Generics', '.kappa_tri', '.isOpen', '.isMethodsDispatchOn', '.Internal', '.handleSimpleError', '.gtn', '.gt', '.GlobalEnv', '.getRequiredPackages2', '.getRequiredPackages', '.getNamespaceInfo', '.getNamespace', '.GenericArgsEnv', '.Fortran', '.format.zeros', '.First.sys', '.find.package', '.F_dtrco', '.F_dqrxb', '.F_dqrrsd', '.F_dqrqy', '.F_dqrqty', '.F_dqrdc2', '.F_dqrcf', '.F_dchdc', '.External2', '.External.graphics', '.External', '.expand_R_libs_env_var', '.encode_numeric_version', '.dynLibs', '.doWrap', '.doTrace', '.doSortWrap', '.difftime', '.Devices', '.Device', '.detach', '.Deprecated', '.deparseOpts', '.Defunct', '.decode_numeric_version', '.Date', '.colSums', '.colMeans', '.col', '.Call.graphics', '.Call', '.cache_class', '.C_R_removeTaskCallback', '.C_R_getTaskCallbackNames', '.C_R_addTaskCallback', '.C', '.bincode', '.BaseNamespaceEnv', '.AutoloadEnv', '.ArgsEnv', '.amatch_costs', '.amatch_bounds', '..getNamespace', '..deparseOpts', '...length', '...elt', '.__S3MethodsTable__.', '.__H__.rbind', '.__H__.cbind', '!=', '!.octmode', '!.hexmode', '!', ':::', '::', ':', '-.POSIXt', '-.Date', '-']



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

#builtins = get_built_ins()
        



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
# XML Parse - find a child with specific tag
#-----------
def findChildrenWithTag(item,tag):
    result = list(item.iterfind(tag))
    for i in item.findall(tag):
        ET.dump(i)
    #print(result)
    lst = []
    if (len(result)):
        #print(len(result))
        for i in range(len(result)):
            print(result[i].text)
            lst.append(result[i].text)
        #print(lst)

        return(lst)
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


def getCallList(itemlist):

    calllist = []
    for item in itemlist:
        calls = getFunctionCalls(item)
        calllist.extend(calls)
    
    #print(calllist)
    return calllist



def getAccessList(itemlist):

    result = []
    i = 0 
    for item in itemlist:

        for id in item.findall('.//expr'):
            etags,etxt,elist = getChildList(id)
            indxs = findSublistIndx(['OP-DOLLAR','SYMBOL'],etags)
            for i in range(len(indxs)): 
                a,b = indxs[i]
                result.append(etxt[a+1])
    
    return list(set(result))

def sublist(lst1, lst2):
    ls1 = [element for element in lst1 if element in lst2]
    ls2 = [element for element in lst2 if element in lst1]
    return ls1 == ls2

def getObjectFields(item):

    objlist = []
    if (item.tag == "expr"):
        etags,etxt,elist = getChildList(item)
        for i in range(len(elist)):
            if (elist[i].tag == "expr"):
                et2,txt2,el2 = getChildList(elist[i])

                if (et2 == ['expr', 'LEFT_ASSIGN', 'expr']):
                    et3,txt3,el3 = getChildList(el2[2])

                    if (sublist(['expr', 'OP-LEFT-PAREN'],et3)):
                        et4,txt4,el4 = getChildList(el3[0])

                        indxs = findSublistIndx(['expr', 'OP-DOLLAR', 'SYMBOL_FUNCTION_CALL'],et4)

                        for i in range(len(indxs)):
                            a,b = indxs[i]
                            obj = findChild(el4[a],'SYMBOL')
                            objlist.append(obj)

    #print(objlist)
    return objlist

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
    accessors = {}
    methodacc = {}
    count = {} # dictionary to hold the count of methods , fields 
    indxs = findSublistIndx(['SYMBOL_SUB', 'EQ_SUB', 'expr'],etags)
    #print(indxs)
    for i in range(len(indxs)):
        inhlist = []

        a,b = indxs[i]
        mtype = etxt[a]    # public , private or active declarations

        if (mtype == 'inherit'):
            subclass = findChild(elist[b],'SYMBOL')
            inhlist.append(subclass)

        tag,txt,el = getChildList(elist[b])
        method_cnt = 0
        field_cnt = 0 

        memdict = {}
        memfunc = []
        memflds = [] 



        calllist = []
        objfields = []

        i2 = findSublistIndx(['SYMBOL_SUB', 'EQ_SUB', 'expr'],tag)
        for i in range(len(i2)):
            a,b = i2[i]
            line1 = el[b].get("line1")
            line2 = el[b].get("line2")
            tag3,txt3,el3 = getChildList(el[b])
            if (tag3[0] == 'FUNCTION'):
                fname = txt[a]
                #print(f"Method    :  {fname} {line1} {line2}")
                calllist.append(fname)
                memfunc.append((fname,line1,line2))  
                method_cnt = method_cnt + 1
                calls = getCallList(el3)
                alist = getAccessList(el3)
                methodacc[fname]=alist
                calllist.extend(calls)

                for a in alist:
                    if a in accessors.keys():
                        accessors[a].append(fname)

                if (fname == "initialize"):
                    for j in range(len(el3)):
                        #ET.dump(el3[j])
                        objfields.extend(getObjectFields(el3[j]))
                #print(calls)
            else:
                #print(f"Member    :  {txt[a]}")
                field_cnt = field_cnt +1 
                memflds.append(txt[a])

                accessors[txt[a]] = []

        #print(accessors)
        calllist = list(set(calllist))
        memdict["methods"] = memfunc
        memdict["fields"] = memflds 
        memdict["methodcalls"] = calllist
        memdict["access"] = accessors  # this dictionary has fields to method mapping
        memdict["methodaccess"] = methodacc # this dictionary has method to fields access
        spl_attrs = ['classname','lock_objects','class','portable','lock_class','cloneable']

        if (len(objfields)):
            members["objects"] = objfields

        if (mtype == "inherit"):
            members[mtype] = inhlist
        elif (mtype not in  spl_attrs):
            members[mtype] = memdict 
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

def readSrcLines(content,start,end):
    #print(content[end])
    txt = "".join(content[start-1:end])
    #print(txt)
    #ftxt = re.split('=(\s)function',txt,1)
    #print(ftxt)

    #functxt = "function"+ txt.split("= function")[1]
    functxt = re.split('= *function',txt,1)
    if (len(functxt) ==2 ):
        functxt = "function" + functxt[1]

    lines = functxt.split("\n")
    txt = [line for line in lines if line.strip() != ""]
    functxt = "\n".join(txt)
    if (functxt[-1]==','):
        functxt = functxt[:-1]

    return functxt

# Run cyclocomp on extracted function text 
def getComplexity(string):
    qstr = r'\".*\"'   # remove quoted strings
    string = re.sub(qstr, '', string )

    string = "\"" + string + "\""

    cmdstr = "library(cyclocomp)"+"\n"+"cyclocomp(" + string + ")"
    cmd = ["Rscript","-e",cmdstr]
    result = subprocess.run(cmd, capture_output=True, encoding='UTF-8')

    if (result.stdout.rstrip() !=""):
        routput = result.stdout.split(' ')[1]
    else:
        routput = 0 
    return(int(routput))

'''LCOM3 = (m — sum(mA)/a) / (m-1) where :
m number of procedures (methods) in class
a number of variables (attributes) in class. a contains all variables whether shared (static) or not.
mA number of methods that access a variable (attribute)'''

def getCohesionMetrics(classdef):
    result = {}


    for i in range(len(classdef)):
        m = 0 
        a = 0 
        mA = 0 

        cname = list(classdef[i].keys())[0]
        val = classdef[i][cname]

        for k in val.keys():
            if (k in [ "inherit" , "objects"]): 
                continue

            mem = val[k]
            m = m + len(mem["methodaccess"].keys())
            a = a + len(mem["access"].keys())

            d = mem["access"]

            for j in d.keys():
                mA = mA + len(d[j])
        
        #print(f"m = {m} a = {a} mA = {mA}")
        if (m > 1):
            lcom = (m - (mA/a)) / (m-1)
        else:
            lcom = 0 

        result[cname] = lcom

    if (len(result)):
        ave = sum(result.values())/len(result)
    else:
        ave = 0 
    return result, ave


def getCouplingMetrics(classdef):

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
                ce[base] = ce[base]+1

                cbo[cname].append(base)
                if (base in cbo.keys()):
                    cbo[base].append(cname)
                else:
                    cbo[base] = []

        if ("objects" in val.keys()):
            
            ca[cname] = ca[cname] + len(val["objects"])
            # update the ce for the CE field for the superclass
            
            for base in val["objects"]: 
                ce[base] = ce[base]+1

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
        if (ce[k]+ca[k]):
            mi[k] = ca[k]/(ca[k]+ce[k])
        else:
            mi[k] = 0 

        cbo[k] = len(cbo[k])

    result["CA"]=ca
    result["CE"]=ce
    result["MI"]= mi
    result["CBO"] = cbo

    if (len(ca)):
        resultave["CA"]= sum(ca.values())/len(ca)
        resultave["CE"]= sum(ce.values())/len(ce)
        resultave["MI"]= sum(mi.values())/len(mi)
        resultave["CBO"]= sum(cbo.values())/len(cbo)
    else:
        resultave["CA"]= 0 
        resultave["CE"]= 0
        resultave["MI"]= 0 
        resultave["CBO"]= 0 


    #print(f"CBO {cbo}")
    return result , resultave


def processxml(filename,actualfilename=""):

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

    #get_s4_class_definitions(root)

    total_pub_fields = 0 
    total_pri_fields = 0 

    total_pub_methods = 0 
    total_pri_methods = 0 

    dam = 0 


    totalcomplexity = 0
    avecomplexity = 0 
    methodcallcount = 0 

    #print(cdef)
    #print(count)
    for i in range(len(cdef)):
        cname = list(cdef[i].keys())[0]

        #print(count[i])
        val = count[i][cname]
        #print(val)

        #print(cdef[i])
        if ("public" in val.keys()):
            pnum = val["public"]
            total_pub_fields  = total_pub_fields  + pnum[0] 
            total_pub_methods  = total_pub_methods  + pnum[1] 
        
        if ("private" in val.keys()):
            pnum = val["private"]
            total_pri_fields  = total_pri_fields  + pnum[0] 
            total_pri_methods  = total_pri_methods  + pnum[1] 
        

        if (total_pri_fields + total_pub_fields):
            dam = total_pri_fields / (total_pri_fields + total_pub_fields)

        valdict = (list(cdef[i].values())[0])
        
        methodcallcount = 0 
        totalcomplexity = 0
        methodcount = 0 
        for item in valdict.values():
            
            if (isinstance(item,dict)):
                methodcalls = item["methodcalls"]
                methodcallcount += len(methodcalls)
                funclist = item["methods"]
                for func in funclist:
                    funcname,line1,line2 = func
                    start = int(line1)
                    end = int(line2)

                    functxt = readSrcLines(content,start,end)
                    c = getComplexity(functxt)

                    totalcomplexity = totalcomplexity + c
                    methodcount = methodcount + 1 

        avecomplexity = (totalcomplexity / methodcount)

    cm , cmave = getCouplingMetrics(cdef)
    coh , ave = getCohesionMetrics(cdef)

    # Cohesion CBO,LCOM, CAM 

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

    for k in cmave.keys():
        metrics[k] = cmave[k]
    metrics["LCOM"] = ave
    metrics["Coupling"] = "Coupling Metrics" #str(cm)
    metrics["Cohesion"] = "Cohesion Metrics" #str(coh)
    return metrics


def main():

    result = {}

    metrics = []
    result["SCRIPT_VERSION"] = ScriptVersion

    if __name__ == "__main__":
        #print(f"Arguments count: {len(sys.argv)}")
        for i, arg in enumerate(sys.argv):
            if (i>0):
                rscript = ["Rscript","RParse.R",arg,"temp.xml"]
                print(f"Processing file {arg}")
                #print(rscript)
                cmd = subprocess.run(rscript,capture_output=True, encoding='UTF-8')

                if (cmd.returncode):
                    print(f"Parser Error in {arg}")
                    print(cmd.stderr)
                else:
                    filemetric = processxml("temp.xml",arg)
                    metrics.append(filemetric)
    

    result["metrics"] = metrics

    print("Generating  file metrics.json ...")
    with open('metrics.json', 'w') as fp:
        json.dump(result, fp)


main()
