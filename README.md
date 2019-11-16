# Chernobyl

A game of cleaning radioactive environment by collecting contaminated particles.

## Objective

Game is scientifically accurate but fun game about finding Bluetooth
or Wi-Fi beacons which represent radioactive sources. The objective is
to clean the environment and get as small dose of radioactivity as
possible. You have to act fast but avoid near exposure to the sources.

In addition having fun with this game you also learn how radio waves
propagates.

## Android application

See https://github.com/klokik/Junction2019Beacons for Android
application. It is not using the same engine as this version yet but
is much more playable.

## Desktop version

This repository has desktop version which will be ported to mobile
platforms after Junction. This doesn't use prerecorded samples but all
audio is rendered in real time using scientifically sound algoritms.

### Processing RSSI to "mock radiation"

[RSSI](https://en.wikipedia.org/wiki/Received_signal_strength_indication)
is logaritmic unit which is converted to linear units. All beacon
powers are then combined by summing.

Power is then integrated into dose by summing the values. It is
converted to Sieverts by multiplying it with a magic number.

If your Bluetooth radio sensitivity needs to be adjusted for a fair
gameplay, see variable *sensitivity* in
[prototype/Radioactivity.java](Radioactivity.java). Note the units are
linear so add one zero to get 10 decibels more.

### Sound generation

In sound generation we realistically emulate a piezo speaker. First, a
waveform which has a peak lasting only one sample is generated. Then
it is applied to a FIR filter which does a band-pass between 1500
... 4000 Hz which simulates the physical properties of the speaker
case.

This resusts a "retro" sound because many modern radiation meters have
oscillator and a speaker instead of this. But we want the sound of the
1980s.

In case you want to tune your speaker, feel free to regenerate FIR
filter. Filter parameters are [attached](prototype/geiger_filter.txt)
and they work with
[GNU Radio Filter Design Tool](http://www.trondeau.com/home/2012/12/19/update-on-filter-design-tool.html).

### Running

Project doesn't yet have a build file so running is a bit manual:

```sh
cd prototype
javac Receiver.java
sudo btmon | java Receiver BEACON_MACS...
```

RSSI is read from Bluetooth stack temporarily using `btmon` until we
get a better solution. After that we no longer require `sudo`.

The game supports multiple beacons, in that case list all beacon MAC
addresses separated by spaces. You can obtain MACs using `btmon`,
[iBKS Config Tool on Android](https://play.google.com/store/apps/details?id=com.accent_systems.ibks_config_tool),
or any other bluetooth scanning tool.

Remember to specify at least one MAC address. Otherwise you get zero
dose and stay healty for the rest of your life.

## License

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at
your option) any later version. See [license file](LICENSE)
