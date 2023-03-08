package it.unimi.distanceinair.server.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class BasicConverter {
    private final ModelMapper modelMapper = new ModelMapper();

    public BasicConverter() {}

    public <D> D map(Object source, Class<D> destinationType) {
        return this.modelMapper.map(source, destinationType);
    }
}
