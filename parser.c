#include <stdio.h>
#include <stdlib.h>
#include "parser.h"

int readgraph(const char *filename,graph *g) {
  FILE *f=fopen(filename,"r");
if(f==NULL) 
return 1;

int edge_count=0;
char buff[100];

init_graph(g,edge_count+1,edge_count);

rewind(f);
for(int i=0;i<edge_count,i++){
  fscanf(f,"%*s %d %d %lf",&g->edges[i].u,&g->edges[i].v,&g->edges[i].weight);
  }
fclose(f);
return 0;
}
