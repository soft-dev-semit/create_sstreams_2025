package csit.semit.createliststudystreams.service;

import csit.semit.createliststudystreams.entity.StreamsAutumn;
import csit.semit.createliststudystreams.repository.StreamsAutumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamsAutumnServiceImpl implements StreamsAutumnService {

    private final StreamsAutumnRepository streamsAutumnRepository;

    @Autowired
    public StreamsAutumnServiceImpl(StreamsAutumnRepository streamsAutumnRepository) {
        this.streamsAutumnRepository = streamsAutumnRepository;
    }

    @Override
    public StreamsAutumn createStreamsAutumn(StreamsAutumn streamsAutumn) {
        return streamsAutumnRepository.save(streamsAutumn);
    }

    @Override
    public StreamsAutumn getStreamsAutumnById(Long id) {
        return streamsAutumnRepository.findById(id).orElse(null);
    }

    @Override
    public StreamsAutumn getStreamsAutumnByName(String name) {
        return streamsAutumnRepository.findByNameStream(name);
    }

    @Override
    public List<StreamsAutumn> getAllStreamsAutumn() {
        return (List<StreamsAutumn>) streamsAutumnRepository.findAll();
    }

    @Override
    public StreamsAutumn updateStreamsAutumn(Long id, StreamsAutumn updatedStreamsAutumn) {
        StreamsAutumn existingCountry = streamsAutumnRepository.findById(id).orElse(null);
        if (existingCountry != null) {
            updatedStreamsAutumn.setId(existingCountry.getId());
            return streamsAutumnRepository.save(updatedStreamsAutumn);
        }
        return null;
    }

    @Override
    public StreamsAutumn updateStreamsAutumn(StreamsAutumn updatedStreamsAutumn) {
        return streamsAutumnRepository.save(updatedStreamsAutumn);
    }

    @Override
    public void deleteStreamsAutumn(Long id) {
        streamsAutumnRepository.deleteById(id);
    }

    @Override
    public void clearStreamsAutumn() {
        streamsAutumnRepository.deleteAll();
    }
}
