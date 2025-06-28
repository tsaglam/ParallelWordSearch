library(ggplot2)
library(extrafont)
library(dplyr)

source("data_processing.R")

font_import()
loadfonts(quiet = TRUE)

# Preparation:
data <- read_from_csv()
data <- data[data$name != "PrefixHashing", ]
data <- data[data$name != "ParallelPrefixForest", ]
#data <- data[data$size <500, ]

data$name <- factor(
  data$name,
  levels = c(
    "Naive",
    "ParallelStream",
    "TreeSet",
    "MultiTreeSet",
    "ParallelPrefixTree"
  )
)

approach_colors <- c(
  "Naive" = "#F8766D",
  "ParallelStream" = "#C49A00",
  "TreeSet" = "#00B6EB",
  "MultiTreeSet" = "#AA4499",
  "ParallelPrefixTree" = "#117733"
)

approach_linetypes <- c(
  "Naive" = "solid",
  "ParallelStream" = "solid",
  "TreeSet" = "solid",
  "MultiTreeSet" = "solid",
  "ParallelPrefixTree" = "dashed" ,
  "ParallelPrefixForest" = "solid"
)

width <- 7.5
height <- 3.5

plot_and_save <- function(label) {
  file_name <-
    paste('./output/performance_', label, '.pdf', sep = "")
  ggsave(
    file_name,
    device = cairo_pdf,
    width = width,
    height = height,
    units = "in"
  )
}

ggplot(data, aes(
  x = size,
  y = time,
  color = name,
  linetype = name
)) +
  geom_line(size = 0.5) +
  geom_point(size = 1) +
  scale_color_manual(values = approach_colors, drop = FALSE) +  # Important!
  scale_linetype_manual(values = approach_linetypes, drop = FALSE) +  # <- here
  coord_cartesian(ylim = c(0, 4.25)) +
  labs(x = "No. of Searches", y = "Time in S", color = "Approach") +
  guides(linetype = "none") +
  theme(
    text = element_text(size = 12),
    legend.key = element_rect(fill = "white", colour = "white"),
    legend.position = "bottom",
    legend.margin = margin(
      t = 0,
      r = 0,
      b = 0,
      l = 0
    )
  )
plot_and_save("create_and_search")
