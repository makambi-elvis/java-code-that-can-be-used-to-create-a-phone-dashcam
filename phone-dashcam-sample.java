import java.io.File;
import java.io.IOException;
import javax.media.*;
import javax.media.format.VideoFormat;

public class VideoRecorder {

    private final int NUM_SECONDS_TO_SAVE = 300; // 5 minutes
    private final String FILE_EXTENSION = "mp4";

    private boolean recording;
    private MediaLocator locator;
    private DataSink fileWriter;
    private DataSource source;

    public VideoRecorder(String filePath) {
        try {
            locator = new 

MediaLocator("javasound://44100");
            source = Manager.createDataSource(locator);
            fileWriter = Manager.createDataSink(source, new MediaLocator("file:" + filePath));
            fileWriter.open();
        } catch (IOException | NoDataSinkException | NoDataSourceException e) {
            e.printStackTrace();
        }
    }

    public void startRecording() {
        try {
            fileWriter.start();
            recording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopRecording() {
        try {
            fileWriter.stop();
            fileWriter.close();
            recording = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLast5Minutes() {
        File file = new File(locator.getRemainder());
        long length = file.length();
        long start = length - NUM_SECONDS_TO_SAVE * 1000000; // assuming video is in mp4 format
        if (start < 0) {
            start = 0;

        }
        try {
            File newFile = new File(file.getParent(), "Last5Minutes." + FILE_EXTENSION);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(start);
            FileOutputStream out = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = raf.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            raf.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isRecording() {
        return recording;
    }
}
