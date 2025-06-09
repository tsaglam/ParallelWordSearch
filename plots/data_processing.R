library(reshape2)

# https://stackoverflow.com/questions/9564489/read-all-files-in-a-folder-and-apply-a-function-to-each-data-frame
read_from_csv <- function() {
  filenames <-
    list.files("./input/", pattern = "*.csv", full.names = TRUE)
  dataframes <- lapply(filenames, read.csv, sep = ";")
  id_vars <-
    c("name",
      "size",
      "time")
  melted_dataframes <- lapply(dataframes, melt, id.vars = id_vars)
  
  data_all <- do.call("rbind", melted_dataframes)
  return(data_all)
}
