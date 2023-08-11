import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ZipCreator {

  public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.print("Ingresa la ruta del directorio fuente: ");
      String sourceDirectory = scanner.nextLine();

      System.out.print("Ingresa el nombre del archivo ZIP de salida: ");
      String outputFileName = scanner.nextLine();

      File source = new File(sourceDirectory);
      File output = new File(outputFileName);

      try (FileOutputStream fos = new FileOutputStream(output);
          ZipOutputStream zos = new ZipOutputStream(fos)) {

        zipDirectory(source, source, zos);

        System.out.println("Archivo ZIP creado exitosamente.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void zipDirectory(File rootDir, File source, ZipOutputStream zos) throws IOException {
    byte[] buffer = new byte[1024];

    File[] files = source.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          zipDirectory(rootDir, file, zos);
        } else {
          String entryPath = file.getCanonicalPath().substring(rootDir.getCanonicalPath().length() + 1);
          ZipEntry entry = new ZipEntry(entryPath);
          zos.putNextEntry(entry);

          try (FileInputStream fis = new FileInputStream(file)) {
            int length;
            while ((length = fis.read(buffer)) > 0) {
              zos.write(buffer, 0, length);
            }
          }
          zos.closeEntry();
        }
      }
    }
  }
}