package br.dev.robsonsouza.planner.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException() {
        super("Viagem não encontrada");
    }
}
