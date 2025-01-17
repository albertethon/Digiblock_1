package de.neemann.digiblock.Verification;

import de.neemann.digiblock.core.element.Keys;
import de.neemann.digiblock.core.extern.ProcessStarter;
import de.neemann.digiblock.gui.Settings;
import de.neemann.digiblock.lang.Lang;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationIverilogTb {
    private String iverilogFolder;
    private String iverilog;
    private String vvp;
    private final boolean hasIverilog;

    public ApplicationIverilogTb() {
        iverilogFolder = "";
        hasIverilog = findIVerilog();
    }

    public String execIvl(File rtl, File tb) throws IOException {

        if (!hasIverilog) {
            throw new IOException(Lang.get("err_iverilogNotInstalled"));
        }
        try {
            String testOutputName = "test.out";

            return ProcessStarter.start(rtl.getParentFile(), iverilog, "-y","","-tvvp", "-o" + testOutputName, rtl.getName(), tb.getName());
        } catch (IOException e) {
            if (iverilogNotFound(e))
                throw new IOException(Lang.get("err_iverilogNotInstalled"));
            else
                throw e;
        }
    }

    public String execVpp(File rtl) throws IOException {
        if(!hasIverilog) {
            throw new IOException(Lang.get("err_iverilogNotInstalled"));
        }
        try {
            return ProcessStarter.start(rtl.getParentFile(), vvp, Paths.get(rtl.getAbsolutePath()).getParent().resolve("test.out").toString());
        } catch (IOException e) {
            if (iverilogNotFound(e))
                throw new IOException(Lang.get("err_iverilogNotInstalled"));
            else
                throw e;
        }
    }

    private boolean findIVerilog() {
        Path ivp = null;
        File ivDir = Settings.getInstance().get(Keys.SETTINGS_IVERILOG_PATH);

        if (ivDir != null) {
            Path p = Paths.get(ivDir.getAbsolutePath());

            if (Files.isExecutable(p)) {
                ivp = p;
                if (Files.isSymbolicLink(p)) {
                    try {
                        ivp = Files.readSymbolicLink(ivp);
                    } catch (IOException ex) {
                        return false;
                    }
                }
            }
        }

        if (ivp == null) {
            // Let's try to find iverilog in the system path
            String[] strPaths = System.getenv("PATH").split(File.pathSeparator);

            for (String sp : strPaths) {
                Path p = Paths.get(sp, "iverilog");

                if (Files.isExecutable(p)) {
                    ivp = p;
                    if (Files.isSymbolicLink(p)) {
                        try {
                            ivp = Files.readSymbolicLink(ivp);
                        } catch (IOException ex) {
                            return false;
                        }
                    }
                    break;
                }
            }
        }

        if (ivp != null) {
            iverilogFolder = ivp.getParent().getParent().toString();
            iverilog = ivp.getParent().resolve("iverilog").toString();
            vvp = ivp.getParent().resolve("vvp").toString();

            return true;
        } else {
            return false;
        }
    }

    private boolean iverilogNotFound(Throwable e) {
        while (e != null) {
            if (e instanceof ProcessStarter.CouldNotStartProcessException)
                return true;
            e = e.getCause();
        }
        return false;
    }

}

