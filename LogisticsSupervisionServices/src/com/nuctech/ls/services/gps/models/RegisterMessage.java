package com.nuctech.ls.services.gps.models;

public class RegisterMessage {

    /**
     * [{"id":"0100",
     * "phone_num":"014736527431",
     * "register":
     * {"maker":"3132333436",
     * "module":"4E4B31312D383038",
     * "serial_id":"535:4848440000",
     * "plate":"3132333435363738"}}]
     */
    private String maker;

    private String module;

    private String serialId;

    private String plate;

    /**
     * @return the maker
     */
    public String getMaker() {
        return maker;
    }

    /**
     * @param maker
     *        the maker to set
     */
    public void setMaker(String maker) {
        this.maker = maker;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module
     *        the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    /**
     * @return the plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @param plate
     *        the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

}
