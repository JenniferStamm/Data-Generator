package de.datageneration.generator;

import de.datageneration.generator.placement.PlacementFailedException;

public abstract class Pipe<T,U> {

    abstract U process(T input) throws PlacementFailedException;
}
