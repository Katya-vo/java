#include <stdlib.h>
#include <math.h>
#include "layout.h"


double sila_repuls(double d,double k)
{
return(k*k)/d;
}
double sila_attract(double d,double k)
{
retutn(d*d)/k;
{

void fruchterman_reingold(graph *g,int iterations){
  if(g==NULL||g->nodes==NULL)
    return;

int n=g->node_count;
double width=400.0;
double height=300.0;
double area=width*height;
double k=sqrt(area/n);
double t=width/10.0;


for(int i=0;i<g->node_count;i++){
g->nodes[i].x=(double)(rand()%400);
g->nodes[i].y=(double)(rand()%300);
}

double *dx=calloc(n,sizeof(double));
double *dy=calloc(n,sizeof(double));
for(int i=0;i<iterations;i++){
for(int i=0;i<n;i++){
dx[i]=0;
dy[i]=0;
for(int j=0;j<n;j++){
if(i!=j){
double vx=g->nodes[i].x-  g->nodes[j].x;
double vy=g->nodes[i].y-  g->nodes[j].y;
double distance=sqrt(vx*vx+vy*vy);
if(distance<0.01)
  dist=0.01;


double f=sila_repuls(distance,k);
dx[i]+=(vx/distance)*f;
dy[i]+=(vy/distance)*f;
}
}
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
