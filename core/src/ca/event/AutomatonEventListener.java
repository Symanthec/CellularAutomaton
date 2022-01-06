package ca.event;

public interface AutomatonEventListener {

    void generationEdited(GenerationEditedEvent event);

    void generationReplaced(GenerationReplacedEvent generationReplacedEvent);

}
