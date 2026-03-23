#ifndef WRITE_H
#define WRITE_H
#include "graph.h"

int savetxt(const char *filename,graph *g);
int savebin(const char *filename,graph *g);
#endif
