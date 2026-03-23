#include "writer.h"
#include <stdio.h>

int savetxt(const char *filename,graph *g){
  if(g==NULL)
    return -1;

FILE *f=fopen(filename,"w");
if(f==NULL)
  return -2;

for(int i=0;i<g->node_count;i++){
fprintf(f,"%d %f %f\n",i,g->nodes[i].x,g->nodes[i].y);
}
fclose(f);
return 0;
}

int savebin(const char *filename,graph *g){
  if(g==NULL)
    return -1;

FILE *f=fopen(filename,"wb");
if(f==NULL)
  return -2;

fwrite(&(g->node_count),sizeof(int),1,f);
fwrite(g->nodes,sizeof(node),g->node_count,f);
fclose(f);
return 0;
}
