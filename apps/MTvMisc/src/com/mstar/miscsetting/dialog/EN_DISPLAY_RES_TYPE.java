package com.mstar.miscsetting.dialog;

    public enum EN_DISPLAY_RES_TYPE
    {
        DISPLAY_RES_MIN(0),
        DISPLAY_SEC32_LE32A_FULLHD(0),
        // For Normal LVDS panel,
        DISPLAY_RES_SXGA(1),                 ///< 1280x1024, Pnl_AU17_EN05_SXGA
        DISPLAY_RES_WXGA(2),                     ///< 1366x768, Pnl_AU20_T200XW02_WXGA,
        DISPLAY_RES_WXGA_PLUS(3),                ///< 1440x900, Pnl_CMO19_M190A1_WXGA, Pnl_AU19PW01_WXGA
        DISPLAY_RES_WSXGA(4),                    ///< 1680x1050, Pnl_AU20_M201EW01_WSXGA,
        DISPLAY_RES_FULL_HD(5),                  ///< 1920x1080, Pnl_AU37_T370HW01_HD, Pnl_CMO216H1_L01_HD.h

        DISPLAY_DACOUT_PAL_MIN(6),
        DISPLAY_DACOUT_576I(6),  //pal
        DISPLAY_DACOUT_576P(7),
        DISPLAY_DACOUT_720P_50(8),
        DISPLAY_DACOUT_1080P_24(9),
        DISPLAY_DACOUT_1080P_25(10),
        DISPLAY_DACOUT_1080I_50(11),
        DISPLAY_DACOUT_1080P_50(12),
        DISPLAY_DACOUT_PAL_MAX(12),

        DISPLAY_DACOUT_NTSC_MIN(13),
        DISPLAY_DACOUT_480I(13), //ntsc
        DISPLAY_DACOUT_480P(14),
        DISPLAY_DACOUT_720P_60(15),
        DISPLAY_DACOUT_1080P_30(16),
        DISPLAY_DACOUT_1080I_60(17),
        DISPLAY_DACOUT_1080P_60(18),
        DISPLAY_DACOUT_NTSC_MAX(19),
        DISPLAY_DACOUT_AUTO(20),

        DISPLAY_CMO_CMO260J2_WUXGA(21),           // 1920*1200
        //For VGA OUTPUT 60HZ
        DISPLAY_VGAOUT_60_MIN(64),
        DISPLAY_VGAOUT_640x480P_60(64),
        DISPLAY_VGAOUT_60_MAX(64),
        //For VGA OUTPUT 50HZ
        //For TTL output
        DISPLAY_TTLOUT_60_MIN (192),
        DISPLAY_TTLOUT_480X272_60(192),
        DISPLAY_TTLOUT_60_MAX(192),
        DISPLAY_RES_MAX_NUM(193);

        private byte retCode;
        private EN_DISPLAY_RES_TYPE(int value)
        {
            this.retCode=(byte)value;
        }

        public byte getRetCode()
        {
            return retCode;
        }
    }

