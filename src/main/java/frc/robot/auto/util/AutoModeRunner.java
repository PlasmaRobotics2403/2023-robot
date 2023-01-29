package frc.robot.auto.util;

import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoModeRunner {

	private AutoMode auto;
	private Thread autoThread;

	/**
	 * This initializes the AutoModeRunner. Remember to run chooseAutoMode before
	 * starting it.
	 * 
	 * @author Nic A
	 */
	public AutoModeRunner() {
		auto = null;
		autoThread = null;
	}

	/**
	 * Selects which auto mode to run.
	 * 
	 * @param auto The auto mode to run
	 * 
	 * @author Nic A
	 */
	public void chooseAutoMode(AutoMode auto) {
		this.auto = auto;
	}

	/**
	 * This starts the AutoMode chosen. This should be run in AutonomousInit only
	 * once.
	 * 
	 * @author Nic A
	 */
	public void start() {
		stop();
		autoThread = new Thread() {
			@Override
			public void run() {
				if (auto != null) {
					try {
						auto.run();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					DriverStation.reportError("Auto mode null", true);
				}
			}
		};
		autoThread.start();
	}

	/**
	 * Stops the auto mode and kills the thread it runs in. This should be run in
	 * TeleopInit to ensure auto code has ended.
	 * 
	 * @author Nic A
	 */
	public void stop() {
		if (auto != null) {
			auto.stop();
		}
		autoThread = null;
	}


	// Returns if the automode thread is still alive

	public Boolean isAlive() {
		return autoThread.isAlive();
	}

}