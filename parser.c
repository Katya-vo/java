#include <stdio.h>
#include <stdlib.h>
#include "graph.h"
#include "parser.h"

int readgraph(const char *filename,graph *g) {
    FILE *f=fopen(filename,"r");
    if (f==NULL) {
    return 1;
    }

    int n=-1;
    int e=0;

    char edge_name[10];
    int u; 
    int v;
    double w;

    while (fscanf(f,"%s %d %d %lf",edge_name,&u,&v,&w)==4) {
        e++;
        if (u>n) 
        n=u;
        if (v>n) 
        n=v;
    }
    if (e==0) {
        fclose(f);
        return 1;
    }
    int n_count=n+1;
    init_graph(g,n_count,e);
    rewind(f);
    
for (int i=0;i<e;i++) {
        if (fscanf(f,"%s %d %d %lf", 
                   edge_name, 
                   &g->edges[i].s, 
                   &g->edges[i].t, 
                   &g->edges[i].weight) != 4) {
            break;
        }
    } 
    fclose(f);
    return 0;
}
