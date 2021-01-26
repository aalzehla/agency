package com._3line.gravity.freedom.utility;

import com._3line.gravity.core.rolemgt.models.Permission;
import com._3line.gravity.core.setting.dto.SettingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
public class PropertyResource implements InitializingBean {


    private Properties prop = new Properties();
    private InputStream input = null;
    private static Map<String, Properties> propMap = new HashMap<>();
    private String[] registeredFiles = new String[]{"response.properties", "angular.properties" ,"constants.properties","nibbs_mesage.properties","sms_messages.properties"};
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getV(String name, String file) {
        if (!Arrays.asList(registeredFiles).contains(file)) {
            try {
                input = getClass().getClassLoader().getResourceAsStream(file);
                // load a properties file
                prop.load(input);
            } catch (NullPointerException ex) {
                logger.info("Null pointer exception\n");
                ex.printStackTrace();
            } catch (FileNotFoundException ex) {
                logger.info("File name %s was not found\n", file);
                ex.printStackTrace();
            } catch (IOException ex) {
                logger.info("File name %s caused IO exception\n", file);
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            String value = prop.getProperty(name);
            return (value == null ? "" : value);
        }

        try {

            Properties p = propMap.get(file);
            String value = p.getProperty(name);

            return (value == null ? "" : value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public ArrayList<String[]> getAll(String file) {

        ArrayList<String[]> all = new ArrayList<String[]>();
        try {

            input = getClass().getClassLoader().getResourceAsStream(file);


            // load a properties file
            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                all.add(new String[]{key, value});
                //System.out.println("Key : " + key + ", Value : " + value);

            }

        } catch (NullPointerException ex) {
            System.out.printf("Null pointer exception\n");
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            System.out.printf("File name %s was not found\n", file);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.printf("File name %s caused IO exception\n", file);
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return all;
    }

    public List<Permission> getAllPermissions(String file) {

        List<Permission> allPermission = new ArrayList<>();
        try {

            input = getClass().getClassLoader().getResourceAsStream(file);
            // load a properties file
            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                Permission permission = new Permission();
                permission.setDescription(key);
                permission.setName(value);
                allPermission.add(permission);
            }

        } catch (NullPointerException ex) {
            logger.info("Null pointer exception\n");
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            System.out.printf("File name %s was not found\n", file);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.printf("File name %s caused IO exception\n", file);
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return allPermission;
    }
    public List<SettingDTO> getAllSettingsFromFile(String file){

        List<SettingDTO> settingDTOS = new ArrayList<>();
        try {

            input = getClass().getClassLoader().getResourceAsStream(file);
            // load a properties file
            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                SettingDTO settingDTO = new SettingDTO();
                settingDTO.setName(key);
                settingDTO.setCode(key);
                settingDTO.setValue(value);
                settingDTO.setEnabled(true);
                settingDTOS.add(settingDTO);            }

        } catch (NullPointerException ex) {
            logger.info("Null pointer exception\n");
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            System.out.printf("File name %s was not found\n", file);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.printf("File name %s caused IO exception\n", file);
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return settingDTOS ;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("setting up application properties");

        for (String p : registeredFiles) {
            Properties dd = new Properties();
            dd.load(getClass().getClassLoader().getResourceAsStream(p));
            propMap.put(p, dd);
            logger.info("property file {} as been loaded into application successfully", p);
        }

    }

}

