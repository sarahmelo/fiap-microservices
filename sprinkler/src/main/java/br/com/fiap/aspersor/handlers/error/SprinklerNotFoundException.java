package br.com.fiap.aspersor.handlers.error;

public class SprinklerNotFoundException extends RuntimeException {
    public SprinklerNotFoundException(String message) {
        super(message);
    }
}
