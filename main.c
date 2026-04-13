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

    FILE *fin=fopen(input_file,"r");
    if (fin==NULL) {
        fprintf(stderr, "blad:nie udalo sie otworzyc pliku %s\n",input_file);
        return 2;
    }

    int n, e;
    if (fscanf(fin, "%d %d", &n, &e) != 2) {
        fprintf(stderr,"blad:niewlasciwy format pliku \n");
        fclose(fin);
        return 2;
    }

if (algo_type == 1) {
        printf("obliczanie algorytmem fruchterman-reingold\n");
        fruchterman_reingold(g,100);
    else if (algo_type==2) {
        printf("obliczanie algorytmem spectral layout\n");
        spectral_layout(g);
    } else {
        fprintf(stderr,"nieznany algorytm %d\n", algo_type);
        free_graph(g);
        free(g);
        return 4;
    }

    FILE *fout = fopen(output_file, "w");
    if (fout == NULL) {
        fprintf(stderr, "blad zapisyвания do pliku %s\n", output_file);
        free_graph(g);
        free(g);
        return 3;
    }

    for (int i = 0; i < g->node_count; i++) {
        fprintf(fout, "%f %f\n", g->nodes[i].x, g->nodes[i].y);
    }
        fclose(fout);
        free_graph(g);
        free(g);
    return 0;
}
