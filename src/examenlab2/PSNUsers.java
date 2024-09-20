/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package examenlab2;

/**
 *
 * @author Administrator
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PSNUsers {
    private RandomAccessFile RAF;
    private HashTable users;

    public enum Trophy {
        PLATINO(5), ORO(3), PLATA(2), BRONCE(1);

        public final int points;

        Trophy(int points) {
            this.points = points;
        }
    }

    public PSNUsers() throws IOException {
        RAF = new RandomAccessFile("psn", "rw");
        this.users = new HashTable();
        reloadHashTable();
    }

    private void reloadHashTable() throws IOException {
        users = new HashTable(); 
        RAF.seek(0);
        while (RAF.getFilePointer() < RAF.length()) {
            long posInicio = RAF.getFilePointer(); 
            String username = RAF.readUTF();
            int puntos = RAF.readInt();
            int trofeos = RAF.readInt();
            boolean activo = RAF.readBoolean();
            if (activo) {
                users.add(username, posInicio);
            }
        }
    }

    public boolean buscarUsername(String username) {
        return users.search(username) != -1;
    }

    public boolean addUser(String username) throws IOException {
        if (buscarUsername(username)) {
            return false; 
        }
        RAF.seek(RAF.length());
        long posInicio = RAF.getFilePointer();
        RAF.writeUTF(username);
        RAF.writeInt(0); 
        RAF.writeInt(0); 
        RAF.writeBoolean(true); 
        users.add(username, posInicio);
        return true; 
    }

    public void deactivateUser(String username) throws IOException {
        long posicion = users.search(username);
        if (posicion != -1) {
            RAF.seek(posicion);
            RAF.readUTF(); 
            RAF.readInt(); 
            RAF.readInt(); 
            RAF.writeBoolean(false); 

            users.remove(username);

            RandomAccessFile tempRAF = new RandomAccessFile("psn_temp", "rw");
            RAF.seek(0);

            while (RAF.getFilePointer() < RAF.length()) {
                long posInicio = RAF.getFilePointer();
                String user = RAF.readUTF();
                int puntos = RAF.readInt();
                int trofeos = RAF.readInt();
                boolean activo = RAF.readBoolean();
                if (activo) {
                    tempRAF.writeUTF(user);
                    tempRAF.writeInt(puntos);
                    tempRAF.writeInt(trofeos);
                    tempRAF.writeBoolean(true);
                }
            }

            tempRAF.close();
            RAF.close();

            java.io.File originalFile = new java.io.File("psn");
            java.io.File tempFile = new java.io.File("psn_temp");

            if (originalFile.delete()) {
                tempFile.renameTo(originalFile);
            }

            eliminarTrofeos(username);

            RAF = new RandomAccessFile("psn", "rw");
            reloadHashTable();
        }
    }

    private void eliminarTrofeos(String username) throws IOException {
        RandomAccessFile tempTrophyFile = new RandomAccessFile("Trofeos_temp", "rw");
        RandomAccessFile trophyFile = new RandomAccessFile("Trofeos", "rw");

        trophyFile.seek(0);
        while (trophyFile.getFilePointer() < trophyFile.length()) {
            String user = trophyFile.readUTF();
            String tipo = trophyFile.readUTF();
            String juego = trophyFile.readUTF();
            String descripcion = trophyFile.readUTF();
            String fecha = trophyFile.readUTF();
            
            if (!user.equals(username)) {
                tempTrophyFile.writeUTF(user);
                tempTrophyFile.writeUTF(tipo);
                tempTrophyFile.writeUTF(juego);
                tempTrophyFile.writeUTF(descripcion);
                tempTrophyFile.writeUTF(fecha);
            }
        }

        trophyFile.close();
        tempTrophyFile.close();

        java.io.File originalTrophyFile = new java.io.File("Trofeos");
        java.io.File tempTrophyFileFinal = new java.io.File("Trofeos_temp");

        if (originalTrophyFile.delete()) {
            tempTrophyFileFinal.renameTo(originalTrophyFile);
        }
    }

    public void addTrophieTo(String username, String trophyGame, String trophyName, Trophy type) throws IOException {
        long posicion = users.search(username);
        if (posicion != -1) {
            RAF.seek(posicion);
            RAF.readUTF();
            int puntos = RAF.readInt();
            int trophies = RAF.readInt();
            puntos += type.points;
            trophies++;
            RAF.seek(posicion + username.length() + 2); 
            RAF.writeInt(puntos);
            RAF.writeInt(trophies);

            try (RandomAccessFile trophyFile = new RandomAccessFile("Trofeos", "rw")) {
                trophyFile.seek(trophyFile.length());
                trophyFile.writeUTF(username);
                trophyFile.writeUTF(type.name());
                trophyFile.writeUTF(trophyGame);
                trophyFile.writeUTF(trophyName);
                SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                trophyFile.writeUTF(fechaFormateada.format(new Date()));
            }
        }
    }

    public String playerInfo(String username) throws IOException {
        long posicion = users.search(username);
        StringBuilder info = new StringBuilder();
        if (posicion != -1) {
            RAF.seek(posicion);
            String user = RAF.readUTF();
            info.append("Usuario: ").append(user).append("\n");

            int puntos = RAF.readInt();
            info.append("Puntos: ").append(puntos).append("\n");

            int trophies = RAF.readInt();
            boolean isActive = RAF.readBoolean();
            info.append("Activo: ").append(isActive ? "Sí" : "No").append("\n");
            info.append("Cantidad de Trofeos: ").append(trophies).append("\n");

            info.append("\nTrofeos:\n");
            try (RandomAccessFile trophyFile = new RandomAccessFile("Trofeos", "rw")) {
                trophyFile.seek(0);
                while (trophyFile.getFilePointer() < trophyFile.length()) {
                    if (trophyFile.readUTF().equals(username)) {
                        String tipo = trophyFile.readUTF();
                        String juego = trophyFile.readUTF();
                        String descripcion = trophyFile.readUTF();
                        String fecha = trophyFile.readUTF();
                        info.append(fecha).append(" - ").append(tipo).append(" - ").append(juego)
                            .append(" - ").append(descripcion).append("\n");
                    } else {
                        trophyFile.readUTF(); // Leer tipo
                        trophyFile.readUTF(); // Leer juego
                        trophyFile.readUTF(); // Leer descripcion
                        trophyFile.readUTF(); // Leer fecha
                    }
                }
            }
        } else {
            return "Usuario no encontrado.";
        }
        return info.toString();
    }
}