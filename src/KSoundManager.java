package com.mecodegoodsomeday.KaPwing;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Oct 31, 2004
 * Time: 9:28:05 PM
 */
// I am an extroverted, intuitive, thinking, judger entj

public class KSoundManager implements Runnable {
    Vector m_events;
    Vector m_freeEvents;
    static Vector s_kSounds;

    Thread m_thread;

    public static KSound getSoundDevice (int num) {
        return (KSound) s_kSounds.elementAt(num);
    }
    public KSoundManager() {
        m_events = new Vector();
        m_freeEvents = new Vector();
        s_kSounds = new Vector();

        MidiDevice.Info[] devs = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < devs.length; i++) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(devs[i]);
                if (!dev.isOpen()) {
                    dev.open();
                }
                Receiver midiIn = dev.getReceiver();
                KSound thisSound = new KSound();
                thisSound.m_name = "" + devs[i];
                thisSound.m_dev = dev;
                thisSound.m_midiIn = midiIn;
                s_kSounds.add(thisSound);
            } catch (Exception e) {
                // dialog: unable to open midi
            }
        }
        m_thread = new Thread(this);
        m_thread.start();
    }

    public synchronized void addEvent(KMidiEvent e) {
        m_events.add(e);
    }

    public synchronized KMidiEvent getFreeEvent() {
        if (m_freeEvents.isEmpty()) {
            return new KMidiEvent(new ShortMessage(), 0);
        }
        KMidiEvent ret = (KMidiEvent) m_freeEvents.remove(0);
        return ret;
    }

    public synchronized void releaseEvent(KMidiEvent e) {
        m_freeEvents.insertElementAt(e, 0);
    }

    public synchronized void playNoteWithDuration(KSound s, int note, int duration, int channel, int velocity) {
        KMidiEvent ev = getFreeEvent();
        ShortMessage msg = ev.msg;
        try {
            msg.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
            s.sendMessageNow(msg);

            long now = System.currentTimeMillis();
            ev.timeStamp = now + duration;
            ev.m_ks = s;
            msg.setMessage(ShortMessage.NOTE_ON, channel, note, 0);
            addEvent(ev);
        } catch (Exception e){
        }
    }

    public void run() {
        boolean done = false;
        while (!done) {
            long now = System.currentTimeMillis();
            synchronized(this) {
                for (Enumeration e = m_events.elements(); e.hasMoreElements(); ) {
                    KMidiEvent ev = (KMidiEvent)e.nextElement();
                    if (ev.timeStamp <= now) {
                        ev.m_ks.sendMessageNow(ev.msg);
                        m_events.remove(ev);
                    }
                }
            }
            try {
                Thread.sleep(5);
            } catch (Exception e) {
                done = true;
            }
        }
    }

    // this guy is a wonderful human:
    // http://www.cems.uwe.ac.uk/~lrlang/plumstone/

    public static void main(String[] argv) {
        try {
            MidiDevice.Info[] devs = MidiSystem.getMidiDeviceInfo();
            System.out.println("found " + devs.length + " Midi devices");
            for (int i = 0; i < devs.length; i++) {
                    System.out.println(i + ":\t" + devs[i]);
            }
            BufferedReader cmdReader = new BufferedReader(new InputStreamReader(System.in));
            String devstr = cmdReader.readLine();
            int devIndex = Integer.parseInt(devstr);
            MidiDevice dev = MidiSystem.getMidiDevice(devs[devIndex]);
            if (!dev.isOpen()) {
                    dev.open();
            }
            Receiver midiIn = dev.getReceiver();
            System.out.println("hey, got receiver: " + midiIn);
            ShortMessage msg = new ShortMessage();
            for (int j = 0; j < 16; j++) {
                    msg.setMessage(ShortMessage.NOTE_ON, 0, 40 + j, 100);
                    midiIn.send(msg, -1);
                    Thread.sleep(200);
                    msg.setMessage(ShortMessage.NOTE_OFF, 0, 0, 0);
                    midiIn.send(msg, -1);
            }
            dev.close();
        } catch (Exception e) {
                e.printStackTrace();
        }
        System.out.println("done");
        System.exit(0);
    }

}

class KMidiEvent {
    KSound m_ks;
    long timeStamp;
    ShortMessage msg;
    public KMidiEvent(ShortMessage ev, long ts) {
        timeStamp = ts;
        msg = ev;
    }
}

class KSound {
    MidiDevice m_dev;
    Receiver m_midiIn;
    String m_name;

    public synchronized void sendMessageNow(ShortMessage msg) {
        // msg.setMessage(ShortMessage.NOTE_ON, 0, 40 + j, 100);
        m_midiIn.send(msg, -1);
    }
}
