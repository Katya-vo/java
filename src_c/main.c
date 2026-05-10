#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "graph.h"
#include "layout.h"
#include "parser.h"

int main(int argc,char *argv[]) {
    char *input_file=NULL;
    char *output_file=NULL;
    char *format="txt"; 
    int algo_type=1;    

    int opt;
    while ((opt=getopt(argc,argv,"i:o:a:f:"))!= -1) {
        switch (opt) {
            case 'i':
                input_file=optarg;
                break;
            case 'o':
                output_file=optarg;
                break;
            case 'a':
                algo_type=atoi(optarg);
                break;
            case 'f':
                format=optarg; 
                break;
            default:
                fprintf(stderr,"uzycie: %s -i input -o output -a 1/2 -f txt/bin\n", argv[0]);
                return 1;
        }
    }

    if (input_file==NULL || output_file==NULL) {
        fprintf(stderr,"brak plika wejsciowego lub wyjsciowego\n");
        return 1;
    }

    graph *g=malloc(sizeof(graph));
        if (g==NULL) 
        return 1;

    if (readgraph(input_file,g) != 0) {
        fprintf(stderr,"blad odczytu pliku %s\n",input_file);
        free(g);
        return 2;
    }

    if (algo_type==1) {
        fruchterman_reingold(g, 100);
    } else if (algo_type==2) {
        spectral_layout(g);
    } else {
        fprintf(stderr,"nieznany algorytm %d\n", algo_type);
        free_graph(g);
        free(g);
        return 4;
    }

    int save_status;
    if (strcmp(format, "bin")==0) {
        save_status=savebin(output_file, g);
    } else {
        save_status=savetxt(output_file, g);
    }

    if (save_status != 0) {
        fprintf(stderr,"blad zapisu do pliku %s\n", output_file);
    }

    free_graph(g);
    free(g);

    return save_status;
}
