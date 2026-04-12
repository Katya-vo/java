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
  distance=0.01;


double f=sila_repuls(distance,k);
dx[i]+=(vx/distance)*f;
dy[i]+=(vy/distance)*f;
}
}
}


fot (int i=0;i<g->edge_count;i++){
int u=g->edges[i].s;
int v=g->edges[i].t;
double vx=g->nodes[u].x - g->nodes[v].x;
double vy=g->nodes[u].y - g->nodes[v].y;
double distance=sqrt(vx*vx+vy*vy);
if(distance<0.01)
distance=0.01;

double f=sila_attract(distance,k);
double shift_x=(vx/distance)*f;
double shift_y=(vy/distance)*f;
dx[u]-=shift_x;
dy[u]-=shift_y;
dx[v]+=shift_x;
dy[v]+=shift_y;
}

for(int i=0;i<n;i++){
double disp_distance=sqrt(dx[i]*dx[i]+dy[i]*dy[i]);
if(disp_distance>0){
double lim_distance=(disp_distance<t)?disp_distance:t;
g->nodes[i].x+=(dx[i]/disp_distance)*lim_distance;
g->nodes[i].y+=(dy[i]/disp_distance)*lim_distance;
}

if(g->nodes[i].x<0)
  g->nodes[i].x=0;
if(g->nodes[i].x>width)
  g->nodes[i].x=width;
if(f->nodes[i].y<0)
  g->nodes[i].y<0;
if(g->nodes[i].y>heighy)
  g->nodes[i].y=height;
}
temp*=0.95;
}
free(dx);
free(dy);

void moc_iteration(double *L,int n,double *vector,int iterations) {
    for (int i=0;i<n; i++) 
      vector[i] = (double)rand() / RAND_MAX;

    for (int i=0; i<iterations; i++) {
        double *next_v = calloc(n, sizeof(double));
        double norm = 0;
        for (int i=0;i<n;i++) {
            for (int j=0;j<n;j++) {
                next_v[i]+=L[i*n+j]*vector[j];
            }
            norm += next_v[i]*next_v[i];
        }
        norm = sqrt(norm);
for (int i=0;i<n;i++) 
vector[i]=next_v[i]/norm;
        free(next_v);
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
