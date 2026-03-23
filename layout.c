#include <stdlib.h>
#include "layout.h"

void fruchterman_reingold(graph *g,int i){
  if(g==NULL||g->nodes==NULL)
    return;



for(int i=0;i<g->node_count;i++){
g->nodes[i].x=(double)(rand()%400);
g->nodes[i].y=(double)(rand()%300);
}


void spectral_layout(graph *g){
if(g==NULL||g->nodes==NULL||g->node_count<2)
  return;

  double start.x=100.0;
  double start.y=100.0;
  doudle koniec.x=300.0;
  double koniec.y=300.0;

double step.x=(koniec.x-start.x)/(g->node_count-1);
double step.y=(koniec.y-start.y)/(g->node_count-1);

for(int i=0;i<g->node_count;i++){
  g->nodes[i].x=start.x+(i*step.x);
  g->nodes[i].y=start.y+(i*step.y);
}
}
