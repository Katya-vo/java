#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "graph.h"
#include "layout.h"

int main(int argc, char *argv[])
{
    if (argc < 5)
    {
        printf("Usage: %s <input.txt> <output> <circle|random><txt|bin>\n", argv[0]);
        return 1;
    }

    int n =0;
    Edge *edges;
    int *exists = NULL;

    int m = read_graph(argv[1], &edges, &n, &exists);

    if (m <= 0)
    {
        printf("Error reading graph\n");
        return 1;
    }
    
    double *x = malloc(n * sizeof(double));
    double *y = malloc(n * sizeof(double));
    if (x == NULL || y == NULL)
    { 
        fprintf(stderr, "Error memmory allocation failed\n");
        free(edges); 
	free(exists);
        if (x) free(x);
        return 1;
}
    
    if (strcmp(argv[3], "circle") == 0) {
        circle_layout(x, y, n);
    } else {
        random_layout(x, y, n);
    }
    
    if (strcmp(argv[4], "bin") == 0)
{
        FILE *out = fopen(argv[2], "wb");
        if (out) {
            int actual_v = 0;
            for (int i = 0; i < n; i++) if (exists[i]) actual_v++;

            fwrite(&actual_v, sizeof(int), 1, out);
            for (int i = 0; i < n; i++) {
                if (exists[i]) {
                    fwrite(&i, sizeof(int), 1, out);                     		    fwrite(&x[i], sizeof(double), 1, out);
                    fwrite(&y[i], sizeof(double), 1, out);
                }
            }
            fclose(out);
            printf("Gotowe (BIN)! Zapisano w: %s\n", argv[2]);
        }
}
    else 
{
    FILE *out = fopen(argv[2], "w");
    if(out)
    {
        int actual_v = 0;
            for (int i = 0; i < n; i++) if (exists[i]) actual_v++;

            fprintf(out, "%d\n", actual_v);             
	    for (int i = 0; i < n; i++)
            {
                if (exists[i]) { 
                    fprintf(out, "%d %.2f %.2f\n", i, x[i], y[i]);
		}
            }
        fclose(out);
        printf("Ready! \n %s\n", argv[2]);
    } 
}

    free(edges);
    free(exists);
    free(x);
    free(y);

    return 0;
}

