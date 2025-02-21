package csit.semit.createliststudystreams.service;


import csit.semit.createliststudystreams.entity.StreamsAutumn;

import java.util.List;

public interface StreamsAutumnService {

    StreamsAutumn createStreamsAutumn(StreamsAutumn counrty);

    StreamsAutumn getStreamsAutumnById(Long id);

    StreamsAutumn getStreamsAutumnByName(String name);

    List<StreamsAutumn> getAllStreamsAutumn();

    StreamsAutumn updateStreamsAutumn(Long id, StreamsAutumn updatedStreamsAutumn);

    StreamsAutumn updateStreamsAutumn(StreamsAutumn updatedStreamsAutumn);
    void deleteStreamsAutumn(Long id);

    void clearStreamsAutumn();
}
