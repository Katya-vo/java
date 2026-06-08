#ifndef GRAPH_H
#define GRAPH_H

typedef struct {
    char name[64];
    int u;
    int v;
    double weight;
} Edge;

int read_graph(const char *filename, Edge **edges, int *n, int **exists);

#endif



