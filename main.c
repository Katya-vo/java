#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "graph.h"
#include "layout.h"

int main(int argc, char *argv[]) {
    char *input_file = NULL;
    char *output_file = NULL;
    char *format = "txt"; 
    int algo_type = 1;    

    int opt;
    while ((opt = getopt(argc, argv, "i:o:a:f:")) != -1) {
        switch (opt) {
            case 'i':
                input_file = optarg;
                break;
            case 'o':
                output_file = optarg;
                break;
            case 'a':
                algo_type = atoi(optarg);
                break;
            case 'f':
                format = optarg; 
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

    printf("konfiguracja uruchomienia:\n");
    printf("plik wejsciowy: %s\n", input_file);
    printf("plik wyjsciowy: %s\n", output_file);
    printf("format zapisu: %s\n", format);

    graph *g = load_graph(input_file); 
    if (g == NULL) {
        fprintf(stderr, "Blad: nie udalo sie wczytac grafu\n");
        return 2;
    }

    if (algo_type==1) {
    printf("obliczanie algorytmem fruchterman-reingold\n");
    } 
    else if (algo_type==2) {
    printf("obliczanie algorytmem spectral layout\n");
    spectral_layout(g);
    }
    
    if (save_graph(g, output_file, format) != 0) {
        fprintf(stderr,"blad zapisywania do pliku %s\n", output_file);
        free_graph(g);
        return 3;
    }
    free_graph(g);
  
    if (algo_type==1) {
        printf("algorytm:fruchterman-reingold\n");
    } else if (algo_type == 2) {
        printf("algorytm:spectral layout\n");
    } else {
        fprintf(stderr,"nieznany algorytm %d\n", algo_type);
        return 4; 
    }

    return 0;
}
