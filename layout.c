#include <stdlib.h>
#include <stdlib.h>
#include <math.h>
#include "layout.h"

void fruchterman_reingold(graph *g,int i){
  if(g==NULL||g->nodes==NULL)
    return;



for(int i=0;i<g->node_count;i++){
g->nodes[i].x=(double)(rand()%400);
g->nodes[i].y=(double)(rand()%300);
}


void spectral_layout(graph *g){
if(g==NULL||g->nodes==NULL)
  return;



double step=2*3.1415/g->node_count;
for(int i=0;i<g->node_count;i++){
g->nodes[i].x=400+200*cos(i*step);
g->nodes[i].y=300+200*sin(i*step);
}
}
