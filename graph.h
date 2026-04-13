#ifndef GRAPH_H
#define GRAPH_H

typedef struct{
int id;
double x,y;
} node;

typedef struct{
int s;
int t;
double weight;
} edge;

typedef struct{
node *nodes;
edge *edges;
int node_count;
int edge_count;
} graph;

void free_graph(graph *g);
void init_graph(graph *g,int n,int e);
#endif
