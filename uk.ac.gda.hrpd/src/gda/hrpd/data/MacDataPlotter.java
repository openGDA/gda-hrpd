/*-
 * Copyright © 2010 Diamond Light Source Ltd., Science and Technology
 * Facilities Council
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package gda.hrpd.data;

import org.eclipse.dawnsci.analysis.api.io.ScanFileHolderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gda.analysis.ScanFileHolder;
import gda.analysis.io.MACLoader;
import gda.jython.InterfaceProvider;
import gda.jython.JythonServerFacade;
import gda.observable.IObserver;
import uk.ac.diamond.scisoft.analysis.SDAPlotter;

public class MacDataPlotter implements IObserver {
	private static final Logger logger = LoggerFactory.getLogger(MacDataPlotter.class);

	@Override
	public void update(Object theObserved, Object changeCode) {
		if (theObserved instanceof MacDataRebinner) {
//			MacDataRebinner rebinner = (MacDataRebinner) theObserved;
			plotData(changeCode.toString());
		}
	}

	/**
	 * plot rebinned data on DataVector panel
	 * 
	 * @param filename
	 *            - the rebinned data file name
	 * @throws IllegalArgumentException
	 */
	public void plotData(String filename) throws IllegalArgumentException {
		if (InterfaceProvider.getCurrentScanController().isFinishEarlyRequested()) {
			return;
		}

		ScanFileHolder sfh = new ScanFileHolder();
		try {
			sfh.load(new MACLoader(filename));
		} catch (ScanFileHolderException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		JythonServerFacade.getInstance().print("Plot data " + filename);
		try {
			SDAPlotter.plot("MAC", sfh.getAxis(0), sfh.getAxis(1));
		} catch (Exception e) {
			logger.error("Could not plot MAC data", e);
		}
	}
}
