#include "graph.h"
#include <stdlib.h>

void init_graph(graph *g, int n, int e) {
    g->node_count = n;
    g->edge_count = e;
    g->nodes = malloc(n * sizeof(node));
    g->edges = malloc(e * sizeof(edge));
}

void free_graph(graph *g) {
    if (g->nodes != NULL) free(g->nodes);
    if (g->edges != NULL) free(g->edges);
}
