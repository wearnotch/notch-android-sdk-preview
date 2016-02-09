package com.wearnotch.service.network;

import android.bluetooth.BluetoothDevice;

import com.wearnotch.framework.NotchDevice;
import com.wearnotch.framework.Session;
import com.wearnotch.framework.Workout;
import com.wearnotch.framework.Measurement;
import com.wearnotch.framework.NotchNetwork;
import com.wearnotch.framework.NotchState;
import com.wearnotch.service.common.Cancellable;
import com.wearnotch.service.common.NotchCallback;
import com.wearnotch.service.common.Stoppable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Public API of the Android Notch Bluetooth service. This API is <i>stateful</i>: it contains the
 * current state of a single Notch Network. (It can be in "initialized", "disconnected",
 * "capturing", etc states.) This means it can't interact with multiple Notch
 * Networks at the same time.
 */
public interface NotchService {

    /**
     * Initialize Bluetooth discovery and search for available Notch Network.
     */
    Cancellable scan(NotchCallback<? super List<BluetoothDevice>> result);


    /**
     * Initialize Bluetooth discovery and search for Notch device that is NOT in the given set of devices.
     */
    Cancellable scanForNew(Set<NotchDevice> excluded,
                           NotchCallback<? super BluetoothDevice> result);

    /**
     * Use currently connected Notch Network for a new Workout.
     * If current Notch Network is not compatible with the new Workout (e.g. more Notches are needed by the new Workout),
     * initiate a new session.
     */
    Cancellable init(Workout workout, NotchCallback<? super NotchNetwork> result);

    /**
     * Connect to all available Notch devices. The resulting Workout will be saved as a
     * new session for the service until another init() invocation, disconnect or shutdown happens.
     */
    Cancellable uncheckedInit(NotchCallback<? super NotchNetwork> result);

    /**
     * Set the color for each member of the Notch Network according to the Workout configuration.
     */
    Cancellable color(NotchCallback<? super Void> result);

    /**
     * Disconnect from the Notch Network.
     */
    Cancellable disconnect(NotchCallback<? super Void> result);

    /**
     * Get current Notch Network. If the init() call is not successful, return "null".
     */
    NotchNetwork getNetwork();

    /**
     * Erase the flash memory of each member of the Notch Network.
     */

    Cancellable erase(NotchCallback<? super Void> result);

    /**
     * Configure each member of the Notch Network for an upcoming session.
     */
    Cancellable configureSteady(boolean showColors, NotchCallback<? super Void> result);

    /**
     * Initiate steady measurement for the Notch Network.
     * Note: this method is timed.
     * The returned value is the descriptor object needed to download the measurement.
     * This method works only if previously
     * configureSteady(boolean, NotchCallback) was called.
     */
    Cancellable steady(NotchCallback<? super Session> result);

    /**
     * Prepare capture by configuring each member of the Notch Network as described 
     * by the Workout (color, frequencies etc.).
     * This method works only if there is no capture(NotchCallback) in progress.
     */
    Cancellable configureCapture(boolean showColors, NotchCallback<? super Void> result);

    /**
     * Start the capture. This method works must be called after
     * prepareCapture(boolean, NotchCallback).
     */
    Cancellable capture(NotchCallback<? super Void> result);


    /**
     * Stop the capture. The returned value is the descriptor object for download().
     * This method works only if there is a capture(NotchCallback) in
     * progress.
     */
    Cancellable stop(NotchCallback<? super Measurement> result);

    /**
     * Prepare fixed length capture by configuring each member of the Notch Network and a timer.
     */
    Cancellable configureTimedCapture(long timerMillis, boolean showColors, NotchCallback<? super Void> result);

    /**
     * Start the capture for the duration specified by prepareTimedCapture.
     * The returned value is the descriptor object for download().
     * This method must be called after prepareCapture(boolean, NotchCallback).
     */
    Stoppable timedCapture(NotchCallback<? super Measurement> result);

    /**
     * Download measurements from all the devices in the Notch Network.
     */
    Cancellable download(File outputFile, Measurement measurement, NotchCallback<? super Void> result);

    /**
     * Get steady measurement data from all the devices in the Notch Network.
     */
    Cancellable getSteadyData(NotchCallback<? super Void> result);

    /**
     * Shut down the Notch Network. This will turn off all the Notch devices in the network.
     */
    Cancellable shutDown(NotchCallback<? super Void> result);


}