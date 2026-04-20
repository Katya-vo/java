#include <string.h>
#include <stdlib.h>
#include <math.h>
#include "layout.h"
#include "graph.h"


double sila_repuls(double d,double k)
{
return(k*k)/d;
}
double sila_attract(double d,double k)
{
return(d*d)/k;
}

void fruchterman_reingold(graph*g, int iterations) {
    if (g==NULL||g->nodes==NULL) 
    return;

    int n=g->node_count;
    double width=400.0,
    double height=300.0;
    double area=width*height;
    double k=sqrt(area/n);
    double t=width/10.0;

    for (int i=0;i<n;i++) {
        g->nodes[i].x=(double)(rand()%400);
        g->nodes[i].y=(double)(rand()%300);
    }

    double *dx=calloc(n,sizeof(double));
    double *dy=calloc(n,sizeof(double));

    for (int it = 0; it < iterations; it++) {
        for (int i=0;i<n;i++) {
            dx[i]=0;dy[i]=0;
            for (int j=0;j<n;j++) {
                if (i!=j) {
                    double vx=g->nodes[i].x-g->nodes[j].x;
                    double vy=g->nodes[i].y-g->nodes[j].y;
                    double distance=sqrt(vx*vx+vy*vy);
                    if (distance<0.01) 
                    distance = 0.01;
                    double f=sila_repuls(distance, k);
                    dx[i]+=(vx/distance)*f;
                    dy[i]+=(vy/distance)*f;
                }
            }
        }


        for (int i=0;i<g->edge_count;i++) {
            int u=g->edges[i].s;
            int v=g->edges[i].t;
            double weight=g->edges[i].weight; 

            double vx=g->nodes[u].x-g->nodes[v].x;
            double vy=g->nodes[u].y-g->nodes[v].y;
            double distance=sqrt(vx * vx + vy * vy);
            if (distance<0.01) 
            distance=0.01;

   
            double f = sila_attract(distance,k)*weight; 
            
            double shift_x = (vx/distance)*f;
            double shift_y = (vy / distance)*f;
            dx[u]-=shift_x; 
            dy[u]-=shift_y;
            dx[v]+=shift_x; 
            dy[v]+=shift_y;
        }

     
        for (int i = 0; i < n; i++) {
            double disp_dist = sqrt(dx[i] * dx[i] + dy[i] * dy[i]);
            if (disp_dist > 0) {
                double lim = (disp_dist < t) ? disp_dist : t;
                g->nodes[i].x += (dx[i] / disp_dist) * lim;
                g->nodes[i].y += (dy[i] / disp_dist) * lim;
            }
            // Границы экрана
            if (g->nodes[i].x<0) 
            g->nodes[i].x=0;
            if (g->nodes[i].x>width) 
            g->nodes[i].x=width;
            if (g->nodes[i].y<0)
            g->nodes[i].y=0;
            if (g->nodes[i].y>height)
            g->nodes[i].y =height;
        }
        t*=0.95; 
    }
    free(dx); 
    free(dy);
}

void moc_iteration(double *L,int n,double *vector,int iterations) {
    for (int i=0;i<n; i++) 
      vector[i] = (double)rand() / RAND_MAX;

    for (int it=0; it<iterations; it++) {
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
int n=g->node_count;
    double *L = (double *)calloc(n*n,sizeof(double));
    if (!L) return;

    for (int i=0;i<g->edge_count;i++) {
        int u=g->edges[i].s;
        int v=g->edges[i].t;
        if (u < n && v < n) {
            L[u*n+v]= -1.0; 
            L[v*n+u]= -1.0;
            L[u*n+u]+= 1.0; 
            L[v*n+v]+= 1.0;
        }
    }
    double *v2=(double *)malloc(n*sizeof(double));
    double *v3=(double *)malloc(n*sizeof(double));

    
    for (int i=0;i<n;i++) {
        v2[i]=sin(i*2.0* M_PI /n);
        v3[i]=cos(i*2.0* M_PI /n);
    }
    double min_x=100.0;
    double max_x=300.0;
    double min_y=100.0;
    double max_y=300.0;
    for (int i=0;i<n;i++) {
        g->nodes[i].x=min_x+(v2[i]+1.0)/2.0*(max_x - min_x);
        g->nodes[i].y=min_y+(v3[i]+1.0)/2.0*(max_y - min_y);
    }

    free(L);
    free(v2);
    free(v3);
}

