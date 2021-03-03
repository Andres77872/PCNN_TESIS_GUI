package UTIL;

import GUI.MejoraImagenCARPETA;
import GUI.MejoraImagenUNITARIO;

import javax.swing.*;
import java.io.*;

import static MAIN.main.PCNN_PATH;
import static MAIN.main.lg;

public class j2p implements Runnable {
    private boolean stop = false, kill = false;
    private String COMANDO = "", TIPO;
    private File fo;

    public final String UNITARIO = "API.U", CARPETA = "API.C";

    public boolean isProcessAlive() {
        return P.isAlive();
    }

    public boolean processExists() {
        return P != null;
    }

    public String getProcessInfo() {
        return P.info().toString();
    }

    public void kill() {
        stop = true;
        kill = true;
        if (P != null && P.isAlive()) {
            lg.showErrorMSG("Proceso terminado " + P.info());
        }
    }

    public void start() {
        stop = false;
        kill = false;
        Thread stdOutReader;
        stdOutReader = new Thread(this);
        stdOutReader.setDaemon(true);
        stdOutReader.start();
    }

    public void restart() {
        stop = false;
        kill = false;
        Thread stdOutReader;
        stdOutReader = new Thread(this);
        stdOutReader.setDaemon(true);
        stdOutReader.start();
    }

    private synchronized String readLine(InputStream in) throws IOException {
        String input = "";
        do {
            int available = in.available();
            if (available == 0) {
                break;
            }
            byte b[] = new byte[available];
            in.read(b);
            input += new String(b, 0, b.length);
        } while (!input.endsWith("\n") && !input.endsWith("\r\n") && !stop);
        return input.trim();
    }

    Process P;

    private void jp(String cmd) throws IOException {
        FileUtil.resetUnitario(PCNN_PATH,fo);
        //        ProcessBuilder PB = new ProcessBuilder("C:\\Users\\Andres\\Anaconda3\\Scripts\\activate.bat","cd..","python.exe", "C:\\Users\\Andres\\Documents\\Python Programas\\java_python\\py.py");
        try (PrintWriter PW_Script = new PrintWriter(new FileOutputStream("S.bat", false))) {
            PW_Script.println(""
                    + "cd C:\\Users\\Andres\\Spyder Proyectos\\PCNN\n"
                    + "call C:\\Users\\Andres\\Anaconda3\\Scripts\\activate.bat OpenCl\n"
                    + "python \"C:\\Users\\Andres\\Spyder Proyectos\\PCNN\\" + TIPO + ".py\" " + cmd + "\n" +
                    "");
        }

        ProcessBuilder PB = new ProcessBuilder("S.bat");
        PB.redirectInput(ProcessBuilder.Redirect.INHERIT);
        PB.redirectOutput(ProcessBuilder.Redirect.PIPE);
        PB.redirectError(ProcessBuilder.Redirect.INHERIT);

        P = PB.start();

        System.setIn(P.getInputStream());


    }

    public synchronized void run() {
        try {
            jp(COMANDO.replace(",", " "));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        while (!stop) {
            if (kill) {
                P.destroyForcibly();
                P = null;
                return;
            }
            if (!P.isAlive()) {
                stop = true;
            }
            try {
                this.wait(100);
            } catch (InterruptedException ie) {
            }
            try {
                if (System.in.available() != 0) {
                    String input = this.readLine(System.in);
                    evtLogListener(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!kill) {
            JFrame g = switch (TIPO) {
                case UNITARIO:
                    yield new MejoraImagenUNITARIO(COMANDO,fo);
                case CARPETA:
                    yield new MejoraImagenCARPETA(COMANDO,fo);
                default:
                    yield null;
            };
            if (g != null) {
                g.setVisible(true);
            }
        }

    }

    public boolean run(String COMANDO, String API,File fo) {
        this.COMANDO = COMANDO;
        this.fo=fo;
        TIPO = API;
        if (!processExists()) {
            start();
        } else if (!isProcessAlive()) {
            restart();
        } else {
            return false;
        }
        return true;
    }

    public void evtLogListener(String text) {

    }
}
