#include <stdio.h>
#include <stdlib.h>
#include "graph.h"
#include "parser.h"

int readgraph(const char *filename,graph *g) {
    FILE *f=fopen(filename,"r");
    if (f==NULL) {
    return 1;
    }

    int n;
    int e;
 
    if (fscanf(f,"%d %d",&n,&e) != 2) {
        fclose(f);
    return 1;
    }
    init_graph(g,n,e);
    char edge_name[10]; 
    for (int i=0;i<e;i++) {
        if (fscanf(f,"%s %d %d %lf",edge_name,&g->edges[i].s,&g->edges[i].t,&g->edges[i].weight)!= 4) {
            break;
        }
    }
    fclose(f);
    return 0;
}
