package iso;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private final String Filepath;
    private int BufferID;
    private int SourceID;
    private boolean isPlaying = false;

    public Sound(String filepath, boolean Loops) {
        this.Filepath = filepath;

        stackPush();
        IntBuffer ChannelBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer SampleRateBuffer = stackMallocInt(1);

        try {
            ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filepath, ChannelBuffer, SampleRateBuffer);


            if (rawAudioBuffer == null) {
                System.out.println("Failed to load Audio file :" + filepath);
                stackPop();
                stackPop();
                return;
            }

            int Channels = ChannelBuffer.get();
            int SampleRate = SampleRateBuffer.get();
            stackPop();
            stackPop();

            int Format = -1;
            if (Channels == 1) {
                Format = AL_FORMAT_MONO16;
            } else if (Channels == 2) {
                Format = AL_FORMAT_STEREO16;
            }

            BufferID = alGenBuffers();
            alBufferData(BufferID, Format, rawAudioBuffer, SampleRate);

            SourceID = alGenSources();
            alSourcei(SourceID, AL_BUFFER, BufferID);
            alSourcei(SourceID, AL_LOOPING, Loops ? 1 : 0);
            alSourcei(SourceID, AL_POSITION, 0);
            alSourcef(SourceID, AL_GAIN, 0.3f);

            free(rawAudioBuffer);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Trying to Import Sound '"+filepath+"'. possibly wrong filetype");
        }
    }

    public void delete() {
        alDeleteSources(SourceID);
        alDeleteBuffers(BufferID);
    }

    public void play() {
        int state = alGetSourcei(SourceID, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(SourceID, AL_POSITION, 0);
        }

        if (!isPlaying) {
            alSourcePlay(SourceID);
            isPlaying = true;
        }
    }

    public void Stop() {
        if (isPlaying) {
            alSourceStop(SourceID);
            isPlaying = false;
        }
    }

    public String getFilepath() {
        return Filepath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(SourceID, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        return isPlaying;
    }
}
