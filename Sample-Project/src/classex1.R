library(R6)

Person <- R6Class("Person",
  public = list(
    name = NULL,
    hair = NULL,
    initialize = function(name = NA, hair = NA) {
      self$name <- name
      self$hair <- hair
      self$greet()
    },
    set_hair = function(val) {
      self$hair <- val
    },
    greet = function() {
      cat(paste0("Hello, my name is ", self$name, ".\n"))
    }
  )
)
#ann <- Person$new("Ann", "black")
Queue <- R6Class("Queue",
  public = list(
    initialize = function(...) {
      for (item in list(...)) {
        self$add(item)
      }
    },
    add = function(x) {
      private$queue <- c(private$queue, list(x))
      invisible(self)
    },
    remove = function() {
      if (private$length() == 0) return(NULL)
      # Can use private$queue for explicit access
      head <- private$queue[[1]]
      private$queue <- private$queue[-1]
      head
    }
  ),
  private = list(
    queue = list(),
    length = function() base::length(private$queue)
  )
)

#q <- Queue$new(5, 6, "foo")
#q$add("something")
#q$add("another thing")
#q$add(17)
#q$remove()
#q$remove()

Numbers <- R6Class("Numbers",
  public = list(
    x = 100
  ),
  active = list(
    x2 = function(value) {
      if (missing(value)) return(self$x * 2)
      else self$x <- value/2
    },
    rand = function() rnorm(1)
  )
)

#n <- Numbers$new()
#n$x
#n$x2 <- 1000

# Note that this isn't very efficient - it's just for illustrating inheritance.
HistoryQueue <- R6Class("HistoryQueue",
  inherit = Queue,
  public = list(
    show = function() {
      cat("Next item is at index", private$head_idx + 1, "\n")
      for (i in seq_along(private$queue)) {
        cat(i, ": ", private$queue[[i]], "\n", sep = "")
      }
    },
    remove = function() {
      if (private$length() - private$head_idx == 0) return(NULL)
      private$head_idx <- private$head_idx + 1
      private$queue[[private$head_idx]]
    }
  ),
  private = list(
    head_idx = 0
  )
)

#hq <- HistoryQueue$new(5, 6, "foo")
#hq$show()
#hq$remove()


Rando <- R6::R6Class(
  classname = "Rando",
  private = list(
    last_random = NULL
  ),
  active = list(
    random = function(value) {
      if (missing(value)) {
        private$last_random <- runif(1)
        private$last_random
      } else {
        stop("Can't set `$random`.", call. = FALSE)
      }
    },
    previous = function(value) {
      if (missing(value)) {
        private$last_random
      }
    }
  )
)
#x <- Rando$new()
#x$random
#x$random
#x$previous


A <- R6Class(
  classname = "A",
  private = list(
    field = "foo",
    method = function() {
      "bar"
    }
  )
)

B <- R6Class(
  classname = "B",
  inherit = A,
  public = list(
    test = function() {
      cat("Field:  ", super$field, "\n", sep = "")
      cat("Method: ", super$method(), "\n", sep = "")
    }
  )
)

#B$new()$test()