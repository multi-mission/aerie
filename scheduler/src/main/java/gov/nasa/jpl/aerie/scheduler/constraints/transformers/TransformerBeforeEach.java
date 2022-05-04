package gov.nasa.jpl.aerie.scheduler.constraints.transformers;

import gov.nasa.jpl.aerie.constraints.model.SimulationResults;
import gov.nasa.jpl.aerie.constraints.time.Windows;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.scheduler.model.Plan;

public class TransformerBeforeEach implements TimeWindowsTransformer {

  private final Duration dur;

  public TransformerBeforeEach(final Duration dur) {
    this.dur = dur;
  }


  @Override
  public Windows transformWindows(final Plan plan, final Windows windows, final SimulationResults simulationResults) {
    var retWin = new Windows(windows);
    retWin = retWin.complement();
    retWin = retWin.removeLast();
    retWin = retWin.contractBy(Duration.ZERO, dur);
    return retWin;
  }
}
