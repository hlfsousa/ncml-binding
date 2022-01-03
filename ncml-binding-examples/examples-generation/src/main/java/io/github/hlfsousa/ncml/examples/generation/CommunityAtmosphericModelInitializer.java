package io.github.hlfsousa.ncml.examples.generation;


// imports >>
import io.github.hlfsousa.ncml.annotation.*;
import io.github.hlfsousa.ncml.declaration.*;
import io.github.hlfsousa.ncml.io.ArrayUtils;
import java.util.*;
import java.util.function.Consumer;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel.*;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO.*;
import ucar.nc2.Dimension;
// << imports

public class CommunityAtmosphericModelInitializer {

    private static final Map<String, Consumer<CommunityAtmosphericModel>> INITIALIZERS = new HashMap<>();

    static {
        INITIALIZERS.put(null, model -> {
            if (model.getConventions() == null) {
                model.setConventions("CF-1.0");
            }
            if (model.getLogname() == null) {
                model.setLogname("olson");
            }
            if (model.getHost() == null) {
                model.setHost("bb0001en");
            }
            if (model.getSource() == null) {
                model.setSource("Interpolated from:/fs/cgd/data0/olson/inputIC/newICeul.cam2.i.0000-09-01-00000.nc::CAM");
            }
            if (model.getCase() == null) {
                model.setCase("cam2run");
            }
            if (model.getTitle() == null) {
                model.setTitle("Interpolated from:/fs/cgd/data0/olson/inputIC/newICeul.cam2.i.0000-09-01-00000.nc::atm ver atm, eul ver v013, case newICeul");
            }
            if (model.getHistory() == null) {
                model.setHistory("\n05/07/03 12:15:34 olson:chinookfe:interpic -t SEP1.T42L26.gaussian.template.nc /fs/cgd/data0/olson/inputIC/newICeul.cam2.i.0000-09-01-00000.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam1/hrtopo/topo.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo-usgs-10min.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo10min.merged_c030506.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo10min.merged_c030506.nc -l cami_0000-09-01_64x128_L26_c030624.nc.new");
            }
            if (model.getMakeRoss() == null) {
                model.setMakeRoss("true");
            }
            if (model.getLatitudes() != null) {
                LatitudesInitializer.initialize(model.getLatitudes());
            }
            if (model.getLongitudes() != null) {
                LongitudesInitializer.initialize(model.getLongitudes());
            }
            if (model.getLev() != null) {
                LevInitializer.initialize(model.getLev());
            }
            if (model.getIlev() != null) {
                IlevInitializer.initialize(model.getIlev());
            }
            if (model.getTime() != null) {
                TimeInitializer.initialize(model.getTime());
            }
            if (model.getHyai() != null) {
                HyaiInitializer.initialize(model.getHyai());
            }
            if (model.getHybi() != null) {
                HybiInitializer.initialize(model.getHybi());
            }
            if (model.getHyam() != null) {
                HyamInitializer.initialize(model.getHyam());
            }
            if (model.getHybm() != null) {
                HybmInitializer.initialize(model.getHybm());
            }
            if (model.getGw() != null) {
                GwInitializer.initialize(model.getGw());
            }
            if (model.getReferencePressure() != null) {
                ReferencePressureInitializer.initialize(model.getReferencePressure());
            }
            if (model.getDateWritten() != null) {
                DateWrittenInitializer.initialize(model.getDateWritten());
            }
            if (model.getTimeWritten() != null) {
                TimeWrittenInitializer.initialize(model.getTimeWritten());
            }
            if (model.getNtrm() != null) {
                NtrmInitializer.initialize(model.getNtrm());
            }
            if (model.getNtrn() != null) {
                NtrnInitializer.initialize(model.getNtrn());
            }
            if (model.getNtrk() != null) {
                NtrkInitializer.initialize(model.getNtrk());
            }
            if (model.getNdbase() != null) {
                NdbaseInitializer.initialize(model.getNdbase());
            }
            if (model.getNsbase() != null) {
                NsbaseInitializer.initialize(model.getNsbase());
            }
            if (model.getNbdate() != null) {
                NbdateInitializer.initialize(model.getNbdate());
            }
            if (model.getNbsec() != null) {
                NbsecInitializer.initialize(model.getNbsec());
            }
            if (model.getMdt() != null) {
                MdtInitializer.initialize(model.getMdt());
            }
            if (model.getNdcur() != null) {
                NdcurInitializer.initialize(model.getNdcur());
            }
            if (model.getNscur() != null) {
                NscurInitializer.initialize(model.getNscur());
            }
            if (model.getDate() != null) {
                DateInitializer.initialize(model.getDate());
            }
            if (model.getDatesec() != null) {
                DatesecInitializer.initialize(model.getDatesec());
            }
            if (model.getNsteph() != null) {
                NstephInitializer.initialize(model.getNsteph());
            }
            if (model.getU() != null) {
                UInitializer.initialize(model.getU());
            }
            if (model.getV() != null) {
                VInitializer.initialize(model.getV());
            }
            if (model.getTemperature() != null) {
                TemperatureInitializer.initialize(model.getTemperature());
            }
            if (model.getQ() != null) {
                QInitializer.initialize(model.getQ());
            }
            if (model.getPS() != null) {
                PSInitializer.initialize(model.getPS());
            }
            if (model.getPHIS() != null) {
                PHISInitializer.initialize(model.getPHIS());
            }
            if (model.getSGH() != null) {
                SGHInitializer.initialize(model.getSGH());
            }
            if (model.getLANDM() != null) {
                LANDMInitializer.initialize(model.getLANDM());
            }
            if (model.getPBLH() != null) {
                PBLHInitializer.initialize(model.getPBLH());
            }
            if (model.getTPERT() != null) {
                TPERTInitializer.initialize(model.getTPERT());
            }
            if (model.getQPERT() != null) {
                QPERTInitializer.initialize(model.getQPERT());
            }
            if (model.getCLOUD() != null) {
                CLOUDInitializer.initialize(model.getCLOUD());
            }
            if (model.getQCWAT() != null) {
                QCWATInitializer.initialize(model.getQCWAT());
            }
            if (model.getTCWAT() != null) {
                TCWATInitializer.initialize(model.getTCWAT());
            }
            if (model.getLCWAT() != null) {
                LCWATInitializer.initialize(model.getLCWAT());
            }
            if (model.getTSICERAD() != null) {
                TSICERADInitializer.initialize(model.getTSICERAD());
            }
            if (model.getTS() != null) {
                TSInitializer.initialize(model.getTS());
            }
            if (model.getTSICE() != null) {
                TSICEInitializer.initialize(model.getTSICE());
            }
            if (model.getTS1() != null) {
                TS1Initializer.initialize(model.getTS1());
            }
            if (model.getTS2() != null) {
                TS2Initializer.initialize(model.getTS2());
            }
            if (model.getTS3() != null) {
                TS3Initializer.initialize(model.getTS3());
            }
            if (model.getTS4() != null) {
                TS4Initializer.initialize(model.getTS4());
            }
            if (model.getSNOWHICE() != null) {
                SNOWHICEInitializer.initialize(model.getSNOWHICE());
            }
            if (model.getLANDFRAC() != null) {
                LANDFRACInitializer.initialize(model.getLANDFRAC());
            }
            if (model.getTBOT() != null) {
                TBOTInitializer.initialize(model.getTBOT());
            }
            if (model.getICEFRAC() != null) {
                ICEFRACInitializer.initialize(model.getICEFRAC());
            }
            if (model.getSICTHK() != null) {
                SICTHKInitializer.initialize(model.getSICTHK());
            }
            if (model.getTSOCN() != null) {
                TSOCNInitializer.initialize(model.getTSOCN());
            }
            if (model.getCLDLIQ() != null) {
                CLDLIQInitializer.initialize(model.getCLDLIQ());
            }
            if (model.getCLDICE() != null) {
                CLDICEInitializer.initialize(model.getCLDICE());
            }
            if (model.getLANDMCOSLAT() != null) {
                LANDMCOSLATInitializer.initialize(model.getLANDMCOSLAT());
            }
            // customGroupInit >>
            // << customGroupInit
        });
        // userGroupInitializers >>
        // << userGroupInitializers
    }

    public static CommunityAtmosphericModel initialize(CommunityAtmosphericModel value) {
        return initialize(null, value);
    }

    public static CommunityAtmosphericModel initialize(String key, CommunityAtmosphericModel value) {
        if (value == null) {
            value = new CommunityAtmosphericModelVO();
        }
        if (!INITIALIZERS.containsKey(key)) {
            key = null;
        }
        INITIALIZERS.get(key).accept(value);
        return value;
    }
    public static class LatitudesInitializer {
        
        private static final Map<String, Consumer<LatitudesVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("latitude");
                }
                if (model.getUnits() == null) {
                    model.setUnits("degrees_north");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_latitudes >>
                // << customInit_latitudes
            });
            // userInitializers_latitudes >>
            // << userInitializers_latitudes
        }

        public static LatitudesVariable<double[]> initialize(LatitudesVariable<double[]> value) {
            return initialize(null, value);
        }

        public static LatitudesVariable<double[]> initialize(String key, LatitudesVariable<double[]> value) {
            if (value == null) {
                value = new LatitudesVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class LongitudesInitializer {
        
        private static final Map<String, Consumer<LongitudesVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("longitude");
                }
                if (model.getUnits() == null) {
                    model.setUnits("degrees_east");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_longitudes >>
                // << customInit_longitudes
            });
            // userInitializers_longitudes >>
            // << userInitializers_longitudes
        }

        public static LongitudesVariable<double[]> initialize(LongitudesVariable<double[]> value) {
            return initialize(null, value);
        }

        public static LongitudesVariable<double[]> initialize(String key, LongitudesVariable<double[]> value) {
            if (value == null) {
                value = new LongitudesVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class LevInitializer {
        
        private static final Map<String, Consumer<LevVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("hybrid level at midpoints (1000*(A+B))");
                }
                if (model.getUnits() == null) {
                    model.setUnits("level");
                }
                if (model.getPositive() == null) {
                    model.setPositive("down");
                }
                if (model.getStandardName() == null) {
                    model.setStandardName("atmosphere_hybrid_sigma_pressure_coordinate");
                }
                if (model.getFormulaTerms() == null) {
                    model.setFormulaTerms("a: hyam b: hybm p0: P0 ps: PS");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_lev >>
                // << customInit_lev
            });
            // userInitializers_lev >>
            // << userInitializers_lev
        }

        public static LevVariable<double[]> initialize(LevVariable<double[]> value) {
            return initialize(null, value);
        }

        public static LevVariable<double[]> initialize(String key, LevVariable<double[]> value) {
            if (value == null) {
                value = new LevVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class IlevInitializer {
        
        private static final Map<String, Consumer<IlevVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("hybrid level at interfaces (1000*(A+B))");
                }
                if (model.getUnits() == null) {
                    model.setUnits("level");
                }
                if (model.getPositive() == null) {
                    model.setPositive("down");
                }
                if (model.getStandardName() == null) {
                    model.setStandardName("atmosphere_hybrid_sigma_pressure_coordinate");
                }
                if (model.getFormulaTerms() == null) {
                    model.setFormulaTerms("a: hyai b: hybi p0: P0 ps: PS");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("ilev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_ilev >>
                // << customInit_ilev
            });
            // userInitializers_ilev >>
            // << userInitializers_ilev
        }

        public static IlevVariable<double[]> initialize(IlevVariable<double[]> value) {
            return initialize(null, value);
        }

        public static IlevVariable<double[]> initialize(String key, IlevVariable<double[]> value) {
            if (value == null) {
                value = new IlevVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TimeInitializer {
        
        private static final Map<String, Consumer<TimeVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("time");
                }
                if (model.getUnits() == null) {
                    model.setUnits("days since 0000-08-29 00:00:00");
                }
                if (model.getCalendar() == null) {
                    model.setCalendar("noleap");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_time >>
                // << customInit_time
            });
            // userInitializers_time >>
            // << userInitializers_time
        }

        public static TimeVariable<double[]> initialize(TimeVariable<double[]> value) {
            return initialize(null, value);
        }

        public static TimeVariable<double[]> initialize(String key, TimeVariable<double[]> value) {
            if (value == null) {
                value = new TimeVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class HyaiInitializer {
        
        private static final Map<String, Consumer<HyaiVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("hybrid A coefficient at layer interfaces");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("ilev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hyai >>
                // << customInit_hyai
            });
            // userInitializers_hyai >>
            // << userInitializers_hyai
        }

        public static HyaiVariable<double[]> initialize(HyaiVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HyaiVariable<double[]> initialize(String key, HyaiVariable<double[]> value) {
            if (value == null) {
                value = new HyaiVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class HybiInitializer {
        
        private static final Map<String, Consumer<HybiVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("hybrid B coefficient at layer interfaces");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("ilev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hybi >>
                // << customInit_hybi
            });
            // userInitializers_hybi >>
            // << userInitializers_hybi
        }

        public static HybiVariable<double[]> initialize(HybiVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HybiVariable<double[]> initialize(String key, HybiVariable<double[]> value) {
            if (value == null) {
                value = new HybiVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class HyamInitializer {
        
        private static final Map<String, Consumer<HyamVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("hybrid A coefficient at layer midpoints");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hyam >>
                // << customInit_hyam
            });
            // userInitializers_hyam >>
            // << userInitializers_hyam
        }

        public static HyamVariable<double[]> initialize(HyamVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HyamVariable<double[]> initialize(String key, HyamVariable<double[]> value) {
            if (value == null) {
                value = new HyamVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class HybmInitializer {
        
        private static final Map<String, Consumer<HybmVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("hybrid B coefficient at layer midpoints");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hybm >>
                // << customInit_hybm
            });
            // userInitializers_hybm >>
            // << userInitializers_hybm
        }

        public static HybmVariable<double[]> initialize(HybmVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HybmVariable<double[]> initialize(String key, HybmVariable<double[]> value) {
            if (value == null) {
                value = new HybmVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class GwInitializer {
        
        private static final Map<String, Consumer<GwVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("gauss weights");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35); 
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_gw >>
                // << customInit_gw
            });
            // userInitializers_gw >>
            // << userInitializers_gw
        }

        public static GwVariable<double[]> initialize(GwVariable<double[]> value) {
            return initialize(null, value);
        }

        public static GwVariable<double[]> initialize(String key, GwVariable<double[]> value) {
            if (value == null) {
                value = new GwVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class ReferencePressureInitializer {
        
        private static final Map<String, Consumer<ReferencePressureVariable<Double>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("reference pressure");
                }
                if (model.getUnits() == null) {
                    model.setUnits("Pa");
                }
                // customInit_referencePressure >>
                // << customInit_referencePressure
            });
            // userInitializers_referencePressure >>
            // << userInitializers_referencePressure
        }

        public static ReferencePressureVariable<Double> initialize(ReferencePressureVariable<Double> value) {
            return initialize(null, value);
        }

        public static ReferencePressureVariable<Double> initialize(String key, ReferencePressureVariable<Double> value) {
            if (value == null) {
                value = new ReferencePressureVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class DateWrittenInitializer {
        
        private static final Map<String, Consumer<DateWrittenVariable<char[][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("chars")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                }
                // customInit_date_written >>
                // << customInit_date_written
            });
            // userInitializers_date_written >>
            // << userInitializers_date_written
        }

        public static DateWrittenVariable<char[][]> initialize(DateWrittenVariable<char[][]> value) {
            return initialize(null, value);
        }

        public static DateWrittenVariable<char[][]> initialize(String key, DateWrittenVariable<char[][]> value) {
            if (value == null) {
                value = new DateWrittenVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TimeWrittenInitializer {
        
        private static final Map<String, Consumer<TimeWrittenVariable<char[][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("chars")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                }
                // customInit_time_written >>
                // << customInit_time_written
            });
            // userInitializers_time_written >>
            // << userInitializers_time_written
        }

        public static TimeWrittenVariable<char[][]> initialize(TimeWrittenVariable<char[][]> value) {
            return initialize(null, value);
        }

        public static TimeWrittenVariable<char[][]> initialize(String key, TimeWrittenVariable<char[][]> value) {
            if (value == null) {
                value = new TimeWrittenVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NtrmInitializer {
        
        private static final Map<String, Consumer<NtrmVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("spectral truncation parameter M");
                }
                // customInit_ntrm >>
                // << customInit_ntrm
            });
            // userInitializers_ntrm >>
            // << userInitializers_ntrm
        }

        public static NtrmVariable<Integer> initialize(NtrmVariable<Integer> value) {
            return initialize(null, value);
        }

        public static NtrmVariable<Integer> initialize(String key, NtrmVariable<Integer> value) {
            if (value == null) {
                value = new NtrmVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NtrnInitializer {
        
        private static final Map<String, Consumer<NtrnVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("spectral truncation parameter N");
                }
                // customInit_ntrn >>
                // << customInit_ntrn
            });
            // userInitializers_ntrn >>
            // << userInitializers_ntrn
        }

        public static NtrnVariable<Integer> initialize(NtrnVariable<Integer> value) {
            return initialize(null, value);
        }

        public static NtrnVariable<Integer> initialize(String key, NtrnVariable<Integer> value) {
            if (value == null) {
                value = new NtrnVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NtrkInitializer {
        
        private static final Map<String, Consumer<NtrkVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("spectral truncation parameter K");
                }
                // customInit_ntrk >>
                // << customInit_ntrk
            });
            // userInitializers_ntrk >>
            // << userInitializers_ntrk
        }

        public static NtrkVariable<Integer> initialize(NtrkVariable<Integer> value) {
            return initialize(null, value);
        }

        public static NtrkVariable<Integer> initialize(String key, NtrkVariable<Integer> value) {
            if (value == null) {
                value = new NtrkVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NdbaseInitializer {
        
        private static final Map<String, Consumer<NdbaseVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("base day");
                }
                // customInit_ndbase >>
                // << customInit_ndbase
            });
            // userInitializers_ndbase >>
            // << userInitializers_ndbase
        }

        public static NdbaseVariable<Integer> initialize(NdbaseVariable<Integer> value) {
            return initialize(null, value);
        }

        public static NdbaseVariable<Integer> initialize(String key, NdbaseVariable<Integer> value) {
            if (value == null) {
                value = new NdbaseVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NsbaseInitializer {
        
        private static final Map<String, Consumer<NsbaseVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("seconds of base day");
                }
                // customInit_nsbase >>
                // << customInit_nsbase
            });
            // userInitializers_nsbase >>
            // << userInitializers_nsbase
        }

        public static NsbaseVariable<Integer> initialize(NsbaseVariable<Integer> value) {
            return initialize(null, value);
        }

        public static NsbaseVariable<Integer> initialize(String key, NsbaseVariable<Integer> value) {
            if (value == null) {
                value = new NsbaseVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NbdateInitializer {
        
        private static final Map<String, Consumer<NbdateVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("base date (YYYYMMDD)");
                }
                // customInit_nbdate >>
                // << customInit_nbdate
            });
            // userInitializers_nbdate >>
            // << userInitializers_nbdate
        }

        public static NbdateVariable<Integer> initialize(NbdateVariable<Integer> value) {
            return initialize(null, value);
        }

        public static NbdateVariable<Integer> initialize(String key, NbdateVariable<Integer> value) {
            if (value == null) {
                value = new NbdateVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NbsecInitializer {
        
        private static final Map<String, Consumer<NbsecVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("seconds of base date");
                }
                // customInit_nbsec >>
                // << customInit_nbsec
            });
            // userInitializers_nbsec >>
            // << userInitializers_nbsec
        }

        public static NbsecVariable<Integer> initialize(NbsecVariable<Integer> value) {
            return initialize(null, value);
        }

        public static NbsecVariable<Integer> initialize(String key, NbsecVariable<Integer> value) {
            if (value == null) {
                value = new NbsecVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class MdtInitializer {
        
        private static final Map<String, Consumer<MdtVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("timestep");
                }
                if (model.getUnits() == null) {
                    model.setUnits("s");
                }
                // customInit_mdt >>
                // << customInit_mdt
            });
            // userInitializers_mdt >>
            // << userInitializers_mdt
        }

        public static MdtVariable<Integer> initialize(MdtVariable<Integer> value) {
            return initialize(null, value);
        }

        public static MdtVariable<Integer> initialize(String key, MdtVariable<Integer> value) {
            if (value == null) {
                value = new MdtVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NdcurInitializer {
        
        private static final Map<String, Consumer<NdcurVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current day (from base day)");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_ndcur >>
                // << customInit_ndcur
            });
            // userInitializers_ndcur >>
            // << userInitializers_ndcur
        }

        public static NdcurVariable<int[]> initialize(NdcurVariable<int[]> value) {
            return initialize(null, value);
        }

        public static NdcurVariable<int[]> initialize(String key, NdcurVariable<int[]> value) {
            if (value == null) {
                value = new NdcurVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NscurInitializer {
        
        private static final Map<String, Consumer<NscurVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current seconds of current day");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_nscur >>
                // << customInit_nscur
            });
            // userInitializers_nscur >>
            // << userInitializers_nscur
        }

        public static NscurVariable<int[]> initialize(NscurVariable<int[]> value) {
            return initialize(null, value);
        }

        public static NscurVariable<int[]> initialize(String key, NscurVariable<int[]> value) {
            if (value == null) {
                value = new NscurVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class DateInitializer {
        
        private static final Map<String, Consumer<DateVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current date (YYYYMMDD)");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_date >>
                // << customInit_date
            });
            // userInitializers_date >>
            // << userInitializers_date
        }

        public static DateVariable<int[]> initialize(DateVariable<int[]> value) {
            return initialize(null, value);
        }

        public static DateVariable<int[]> initialize(String key, DateVariable<int[]> value) {
            if (value == null) {
                value = new DateVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class DatesecInitializer {
        
        private static final Map<String, Consumer<DatesecVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current seconds of current date");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_datesec >>
                // << customInit_datesec
            });
            // userInitializers_datesec >>
            // << userInitializers_datesec
        }

        public static DatesecVariable<int[]> initialize(DatesecVariable<int[]> value) {
            return initialize(null, value);
        }

        public static DatesecVariable<int[]> initialize(String key, DatesecVariable<int[]> value) {
            if (value == null) {
                value = new DatesecVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class NstephInitializer {
        
        private static final Map<String, Consumer<NstephVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current timestep");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_nsteph >>
                // << customInit_nsteph
            });
            // userInitializers_nsteph >>
            // << userInitializers_nsteph
        }

        public static NstephVariable<int[]> initialize(NstephVariable<int[]> value) {
            return initialize(null, value);
        }

        public static NstephVariable<int[]> initialize(String key, NstephVariable<int[]> value) {
            if (value == null) {
                value = new NstephVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class UInitializer {
        
        private static final Map<String, Consumer<UVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Zonal wind");
                }
                if (model.getUnits() == null) {
                    model.setUnits("m/s");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_U >>
                // << customInit_U
            });
            // userInitializers_U >>
            // << userInitializers_U
        }

        public static UVariable<double[][][][]> initialize(UVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static UVariable<double[][][][]> initialize(String key, UVariable<double[][][][]> value) {
            if (value == null) {
                value = new UVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class VInitializer {
        
        private static final Map<String, Consumer<VVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Meridional wind");
                }
                if (model.getUnits() == null) {
                    model.setUnits("m/s");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_V >>
                // << customInit_V
            });
            // userInitializers_V >>
            // << userInitializers_V
        }

        public static VVariable<double[][][][]> initialize(VVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static VVariable<double[][][][]> initialize(String key, VVariable<double[][][][]> value) {
            if (value == null) {
                value = new VVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TemperatureInitializer {
        
        private static final Map<String, Consumer<TemperatureVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_temperature >>
                // << customInit_temperature
            });
            // userInitializers_temperature >>
            // << userInitializers_temperature
        }

        public static TemperatureVariable<double[][][][]> initialize(TemperatureVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static TemperatureVariable<double[][][][]> initialize(String key, TemperatureVariable<double[][][][]> value) {
            if (value == null) {
                value = new TemperatureVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class QInitializer {
        
        private static final Map<String, Consumer<QVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Specific humidity");
                }
                if (model.getUnits() == null) {
                    model.setUnits("kg/kg");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_Q >>
                // << customInit_Q
            });
            // userInitializers_Q >>
            // << userInitializers_Q
        }

        public static QVariable<double[][][][]> initialize(QVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static QVariable<double[][][][]> initialize(String key, QVariable<double[][][][]> value) {
            if (value == null) {
                value = new QVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class PSInitializer {
        
        private static final Map<String, Consumer<PSVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Surface pressure");
                }
                if (model.getUnits() == null) {
                    model.setUnits("Pa");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_PS >>
                // << customInit_PS
            });
            // userInitializers_PS >>
            // << userInitializers_PS
        }

        public static PSVariable<double[][][]> initialize(PSVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static PSVariable<double[][][]> initialize(String key, PSVariable<double[][][]> value) {
            if (value == null) {
                value = new PSVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class PHISInitializer {
        
        private static final Map<String, Consumer<PHISVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("surface geopotential");
                }
                if (model.getUnits() == null) {
                    model.setUnits("M2/S2");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)1.e+36); 
                }
                if (model.getFromHires() == null) {
                    model.setFromHires("true");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_PHIS >>
                // << customInit_PHIS
            });
            // userInitializers_PHIS >>
            // << userInitializers_PHIS
        }

        public static PHISVariable<double[][][]> initialize(PHISVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static PHISVariable<double[][][]> initialize(String key, PHISVariable<double[][][]> value) {
            if (value == null) {
                value = new PHISVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class SGHInitializer {
        
        private static final Map<String, Consumer<SGHVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("orography standard deviation");
                }
                if (model.getUnits() == null) {
                    model.setUnits("M");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)1.e+36); 
                }
                if (model.getFromHires() == null) {
                    model.setFromHires("true");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_SGH >>
                // << customInit_SGH
            });
            // userInitializers_SGH >>
            // << userInitializers_SGH
        }

        public static SGHVariable<double[][][]> initialize(SGHVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static SGHVariable<double[][][]> initialize(String key, SGHVariable<double[][][]> value) {
            if (value == null) {
                value = new SGHVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class LANDMInitializer {
        
        private static final Map<String, Consumer<LANDMVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("land ocean transition mask: ocean (0), continent (1), transition (0-1)");
                }
                if (model.getUnits() == null) {
                    model.setUnits("none");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)1.e+36); 
                }
                if (model.getFromHires() == null) {
                    model.setFromHires("true");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_LANDM >>
                // << customInit_LANDM
            });
            // userInitializers_LANDM >>
            // << userInitializers_LANDM
        }

        public static LANDMVariable<double[][][]> initialize(LANDMVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static LANDMVariable<double[][][]> initialize(String key, LANDMVariable<double[][][]> value) {
            if (value == null) {
                value = new LANDMVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class PBLHInitializer {
        
        private static final Map<String, Consumer<PBLHVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("PBL height");
                }
                if (model.getUnits() == null) {
                    model.setUnits("m");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_PBLH >>
                // << customInit_PBLH
            });
            // userInitializers_PBLH >>
            // << userInitializers_PBLH
        }

        public static PBLHVariable<double[][][]> initialize(PBLHVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static PBLHVariable<double[][][]> initialize(String key, PBLHVariable<double[][][]> value) {
            if (value == null) {
                value = new PBLHVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TPERTInitializer {
        
        private static final Map<String, Consumer<TPERTVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Perturbation temperature (eddies in PBL)");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TPERT >>
                // << customInit_TPERT
            });
            // userInitializers_TPERT >>
            // << userInitializers_TPERT
        }

        public static TPERTVariable<double[][][]> initialize(TPERTVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TPERTVariable<double[][][]> initialize(String key, TPERTVariable<double[][][]> value) {
            if (value == null) {
                value = new TPERTVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class QPERTInitializer {
        
        private static final Map<String, Consumer<QPERTVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Perturbation specific humidity (eddies in PBL)");
                }
                if (model.getUnits() == null) {
                    model.setUnits("kg/kg");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_QPERT >>
                // << customInit_QPERT
            });
            // userInitializers_QPERT >>
            // << userInitializers_QPERT
        }

        public static QPERTVariable<double[][][]> initialize(QPERTVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static QPERTVariable<double[][][]> initialize(String key, QPERTVariable<double[][][]> value) {
            if (value == null) {
                value = new QPERTVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class CLOUDInitializer {
        
        private static final Map<String, Consumer<CLOUDVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Cloud fraction");
                }
                if (model.getUnits() == null) {
                    model.setUnits("fraction");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_CLOUD >>
                // << customInit_CLOUD
            });
            // userInitializers_CLOUD >>
            // << userInitializers_CLOUD
        }

        public static CLOUDVariable<double[][][][]> initialize(CLOUDVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static CLOUDVariable<double[][][][]> initialize(String key, CLOUDVariable<double[][][][]> value) {
            if (value == null) {
                value = new CLOUDVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class QCWATInitializer {
        
        private static final Map<String, Consumer<QCWATVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_QCWAT >>
                // << customInit_QCWAT
            });
            // userInitializers_QCWAT >>
            // << userInitializers_QCWAT
        }

        public static QCWATVariable<double[][][][]> initialize(QCWATVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static QCWATVariable<double[][][][]> initialize(String key, QCWATVariable<double[][][][]> value) {
            if (value == null) {
                value = new QCWATVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TCWATInitializer {
        
        private static final Map<String, Consumer<TCWATVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_TCWAT >>
                // << customInit_TCWAT
            });
            // userInitializers_TCWAT >>
            // << userInitializers_TCWAT
        }

        public static TCWATVariable<double[][][][]> initialize(TCWATVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static TCWATVariable<double[][][][]> initialize(String key, TCWATVariable<double[][][][]> value) {
            if (value == null) {
                value = new TCWATVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class LCWATInitializer {
        
        private static final Map<String, Consumer<LCWATVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_LCWAT >>
                // << customInit_LCWAT
            });
            // userInitializers_LCWAT >>
            // << userInitializers_LCWAT
        }

        public static LCWATVariable<double[][][][]> initialize(LCWATVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static LCWATVariable<double[][][][]> initialize(String key, LCWATVariable<double[][][][]> value) {
            if (value == null) {
                value = new LCWATVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TSICERADInitializer {
        
        private static final Map<String, Consumer<TSICERADVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TSICERAD >>
                // << customInit_TSICERAD
            });
            // userInitializers_TSICERAD >>
            // << userInitializers_TSICERAD
        }

        public static TSICERADVariable<double[][][]> initialize(TSICERADVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TSICERADVariable<double[][][]> initialize(String key, TSICERADVariable<double[][][]> value) {
            if (value == null) {
                value = new TSICERADVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TSInitializer {
        
        private static final Map<String, Consumer<TSVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Surface temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TS >>
                // << customInit_TS
            });
            // userInitializers_TS >>
            // << userInitializers_TS
        }

        public static TSVariable<double[][][]> initialize(TSVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TSVariable<double[][][]> initialize(String key, TSVariable<double[][][]> value) {
            if (value == null) {
                value = new TSVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TSICEInitializer {
        
        private static final Map<String, Consumer<TSICEVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Ice temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TSICE >>
                // << customInit_TSICE
            });
            // userInitializers_TSICE >>
            // << userInitializers_TSICE
        }

        public static TSICEVariable<double[][][]> initialize(TSICEVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TSICEVariable<double[][][]> initialize(String key, TSICEVariable<double[][][]> value) {
            if (value == null) {
                value = new TSICEVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TS1Initializer {
        
        private static final Map<String, Consumer<TS1Variable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("TS1      subsoil temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TS1 >>
                // << customInit_TS1
            });
            // userInitializers_TS1 >>
            // << userInitializers_TS1
        }

        public static TS1Variable<double[][][]> initialize(TS1Variable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TS1Variable<double[][][]> initialize(String key, TS1Variable<double[][][]> value) {
            if (value == null) {
                value = new TS1VO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TS2Initializer {
        
        private static final Map<String, Consumer<TS2Variable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("TS2      subsoil temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TS2 >>
                // << customInit_TS2
            });
            // userInitializers_TS2 >>
            // << userInitializers_TS2
        }

        public static TS2Variable<double[][][]> initialize(TS2Variable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TS2Variable<double[][][]> initialize(String key, TS2Variable<double[][][]> value) {
            if (value == null) {
                value = new TS2VO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TS3Initializer {
        
        private static final Map<String, Consumer<TS3Variable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("TS3      subsoil temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TS3 >>
                // << customInit_TS3
            });
            // userInitializers_TS3 >>
            // << userInitializers_TS3
        }

        public static TS3Variable<double[][][]> initialize(TS3Variable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TS3Variable<double[][][]> initialize(String key, TS3Variable<double[][][]> value) {
            if (value == null) {
                value = new TS3VO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TS4Initializer {
        
        private static final Map<String, Consumer<TS4Variable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("TS4      subsoil temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TS4 >>
                // << customInit_TS4
            });
            // userInitializers_TS4 >>
            // << userInitializers_TS4
        }

        public static TS4Variable<double[][][]> initialize(TS4Variable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TS4Variable<double[][][]> initialize(String key, TS4Variable<double[][][]> value) {
            if (value == null) {
                value = new TS4VO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class SNOWHICEInitializer {
        
        private static final Map<String, Consumer<SNOWHICEVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Water equivalent snow depth");
                }
                if (model.getUnits() == null) {
                    model.setUnits("m");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_SNOWHICE >>
                // << customInit_SNOWHICE
            });
            // userInitializers_SNOWHICE >>
            // << userInitializers_SNOWHICE
        }

        public static SNOWHICEVariable<double[][][]> initialize(SNOWHICEVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static SNOWHICEVariable<double[][][]> initialize(String key, SNOWHICEVariable<double[][][]> value) {
            if (value == null) {
                value = new SNOWHICEVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class LANDFRACInitializer {
        
        private static final Map<String, Consumer<LANDFRACVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("gridbox land fraction");
                }
                if (model.getUnits() == null) {
                    model.setUnits("FRAC");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)1.e+36); 
                }
                if (model.getFromHires() == null) {
                    model.setFromHires("true");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_LANDFRAC >>
                // << customInit_LANDFRAC
            });
            // userInitializers_LANDFRAC >>
            // << userInitializers_LANDFRAC
        }

        public static LANDFRACVariable<double[][][]> initialize(LANDFRACVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static LANDFRACVariable<double[][][]> initialize(String key, LANDFRACVariable<double[][][]> value) {
            if (value == null) {
                value = new LANDFRACVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TBOTInitializer {
        
        private static final Map<String, Consumer<TBOTVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Lowest model level temperature");
                }
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TBOT >>
                // << customInit_TBOT
            });
            // userInitializers_TBOT >>
            // << userInitializers_TBOT
        }

        public static TBOTVariable<double[][][]> initialize(TBOTVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TBOTVariable<double[][][]> initialize(String key, TBOTVariable<double[][][]> value) {
            if (value == null) {
                value = new TBOTVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class ICEFRACInitializer {
        
        private static final Map<String, Consumer<ICEFRACVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Fraction of sfc area covered by sea-ice");
                }
                if (model.getUnits() == null) {
                    model.setUnits("fraction");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_ICEFRAC >>
                // << customInit_ICEFRAC
            });
            // userInitializers_ICEFRAC >>
            // << userInitializers_ICEFRAC
        }

        public static ICEFRACVariable<double[][][]> initialize(ICEFRACVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static ICEFRACVariable<double[][][]> initialize(String key, ICEFRACVariable<double[][][]> value) {
            if (value == null) {
                value = new ICEFRACVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class SICTHKInitializer {
        
        private static final Map<String, Consumer<SICTHKVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Sea ice thickness");
                }
                if (model.getUnits() == null) {
                    model.setUnits("m");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_SICTHK >>
                // << customInit_SICTHK
            });
            // userInitializers_SICTHK >>
            // << userInitializers_SICTHK
        }

        public static SICTHKVariable<double[][][]> initialize(SICTHKVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static SICTHKVariable<double[][][]> initialize(String key, SICTHKVariable<double[][][]> value) {
            if (value == null) {
                value = new SICTHKVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class TSOCNInitializer {
        
        private static final Map<String, Consumer<TSOCNVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_TSOCN >>
                // << customInit_TSOCN
            });
            // userInitializers_TSOCN >>
            // << userInitializers_TSOCN
        }

        public static TSOCNVariable<double[][][]> initialize(TSOCNVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static TSOCNVariable<double[][][]> initialize(String key, TSOCNVariable<double[][][]> value) {
            if (value == null) {
                value = new TSOCNVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class CLDLIQInitializer {
        
        private static final Map<String, Consumer<CLDLIQVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Grid box averaged liquid condensate amount");
                }
                if (model.getUnits() == null) {
                    model.setUnits("kg/kg");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_CLDLIQ >>
                // << customInit_CLDLIQ
            });
            // userInitializers_CLDLIQ >>
            // << userInitializers_CLDLIQ
        }

        public static CLDLIQVariable<double[][][][]> initialize(CLDLIQVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static CLDLIQVariable<double[][][][]> initialize(String key, CLDLIQVariable<double[][][][]> value) {
            if (value == null) {
                value = new CLDLIQVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class CLDICEInitializer {
        
        private static final Map<String, Consumer<CLDICEVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("Grid box averaged ice condensate amount");
                }
                if (model.getUnits() == null) {
                    model.setUnits("kg/kg");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_CLDICE >>
                // << customInit_CLDICE
            });
            // userInitializers_CLDICE >>
            // << userInitializers_CLDICE
        }

        public static CLDICEVariable<double[][][][]> initialize(CLDICEVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static CLDICEVariable<double[][][][]> initialize(String key, CLDICEVariable<double[][][][]> value) {
            if (value == null) {
                value = new CLDICEVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

    public static class LANDMCOSLATInitializer {
        
        private static final Map<String, Consumer<LANDMCOSLATVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("land ocean transition mask: ocean (0), continent (1), transition (0-1)");
                }
                if (model.getUnits() == null) {
                    model.setUnits("none");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)1.e+36); 
                }
                if (model.getFromHires() == null) {
                    model.setFromHires("true");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setIsShared(true)
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_LANDM_COSLAT >>
                // << customInit_LANDM_COSLAT
            });
            // userInitializers_LANDM_COSLAT >>
            // << userInitializers_LANDM_COSLAT
        }

        public static LANDMCOSLATVariable<double[][][]> initialize(LANDMCOSLATVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static LANDMCOSLATVariable<double[][][]> initialize(String key, LANDMCOSLATVariable<double[][][]> value) {
            if (value == null) {
                value = new LANDMCOSLATVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }

}
