library(ggplot2)
library(extrafont)
library(cowplot)
library(magrittr)
library(dplyr)
library(stringr)
source("data_processing.R")

font_import()
loadfonts(quiet = TRUE)

# Preparation:
data <- read_from_csv()
data <- data[data$name != "Naive", ]
data <- data[data$name != "ParallelStream", ]

data$name <- factor(data$name, levels = c("Naive", "ParallelStream", "TreeSet", "ForestBased", "PrefixHashing"))
levels(data$name)[levels(data$name) == "ForestBased"] <- "MultiTreeSet"

width <- 7.03
height <- 3

plot_and_save <- function(label) {
  file_name <-
    paste('./output/performance_', label, '.pdf', sep = "")
  ggsave(file_name,
         device = cairo_pdf,
         width = width,
         height = height, units = "in")
}


ggplot(data, aes(x = size, y = time, color = name)) +
  geom_line(size = 0.5) +
  geom_point(size = 1) +
  scale_x_continuous(breaks = seq(0, 6000, by = 500), minor_breaks = NULL) +
  labs(
    x = "No. of Searches",
    y = "Time in S (Init. + Search)",
    color = "Approach"
  ) +
  theme_minimal() +
  theme(
    text = element_text(size = 10),
    legend.key = element_rect(fill = "white", colour = "white"),
    legend.position = "bottom"
  ) 
plot_and_save("create_and_search")


