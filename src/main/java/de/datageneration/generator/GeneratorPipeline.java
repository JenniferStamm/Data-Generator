package de.datageneration.generator;

import de.datageneration.generator.placement.PlacementFailedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneratorPipeline<T, U> {

    private List<Pipe<?, ?>> pipes;

    public GeneratorPipeline(Pipe<?, ?>... pipes) {
        this.pipes =  new ArrayList<>(Collections.<Pipe<?, ?>>singletonList(pipes[0]));
        this.pipes.addAll(Arrays.asList(pipes).subList(1, pipes.length));
    }

    public void append(Pipe<?, ?> pipe) {
        this.pipes.add(pipe);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public U process(T input) {
        Object source = input;
        Object target = null;
        try {
            for (Pipe pipe : pipes) {
                target = pipe.process(source);
                source = target;
            }
        } catch (PlacementFailedException e) {
            e.printStackTrace();
            try {
                pipes.get(pipes.size() - 1).process(null);
            } catch (PlacementFailedException e1) {
                e1.printStackTrace();
            }
        }
        return (U) target;
    }
}
