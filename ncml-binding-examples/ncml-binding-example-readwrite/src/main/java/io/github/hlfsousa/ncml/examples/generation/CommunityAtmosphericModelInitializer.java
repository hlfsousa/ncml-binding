package io.github.hlfsousa.ncml.examples.generation;

// imports >>
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel.*;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO.*;
import io.github.hlfsousa.ncml.io.ArrayUtils;
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

    public static class LatitudeInitializer {
        
        private static final Map<String, Consumer<LatitudeVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_latitude >>
                // << customInit_latitude
            });
            // userInitializers_latitude >>
            // << userInitializers_latitude
        }

        public static LatitudeVariable<double[]> initialize(LatitudeVariable<double[]> value) {
            return initialize(null, value);
        }

        public static LatitudeVariable<double[]> initialize(String key, LatitudeVariable<double[]> value) {
            if (value == null) {
                value = new LatitudeVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class LongitudeInitializer {
        
        private static final Map<String, Consumer<LongitudeVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_longitude >>
                // << customInit_longitude
            });
            // userInitializers_longitude >>
            // << userInitializers_longitude
        }

        public static LongitudeVariable<double[]> initialize(LongitudeVariable<double[]> value) {
            return initialize(null, value);
        }

        public static LongitudeVariable<double[]> initialize(String key, LongitudeVariable<double[]> value) {
            if (value == null) {
                value = new LongitudeVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class HybridLevelInitializer {
        
        private static final Map<String, Consumer<HybridLevelVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getUnits() == null) {
                    model.setUnits("level");
                }
                if (model.getPositive() == null) {
                    model.setPositive("down");
                }
                if (model.getStandardName() == null) {
                    model.setStandardName("atmosphere_hybrid_sigma_pressure_coordinate");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35);
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hybrid_level >>
                // << customInit_hybrid_level
            });
            // userInitializers_hybrid_level >>
            INITIALIZERS.put("lev", model -> {
                // the same as default, plus attribute long_name
                INITIALIZERS.get(null).accept(model);
                if (model.getLongName() == null) {
                    model.setLongName("hybrid level at midpoints (1000*(A+B))");
                }
            });
            
            INITIALIZERS.put("ilev", model -> {
                // same attributes as default (copy/paste)...
                if (model.getUnits() == null) {
                    model.setUnits("level");
                }
                if (model.getPositive() == null) {
                    model.setPositive("down");
                }
                if (model.getStandardName() == null) {
                    model.setStandardName("atmosphere_hybrid_sigma_pressure_coordinate");
                }
                if (model.getFillValue() == null) {
                    model.setFillValue((double)9.99999961690316e+35);
                }
                // ... but not the same shape
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("ilev")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // and we set attribute long_name
                if (model.getLongName() == null) {
                    model.setLongName("hybrid level at interfaces (1000*(A+B))");
                }
            });

            // << userInitializers_hybrid_level
        }

        public static HybridLevelVariable<double[]> initialize(HybridLevelVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HybridLevelVariable<double[]> initialize(String key, HybridLevelVariable<double[]> value) {
            if (value == null) {
                value = new HybridLevelVO();
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
    public static class HybridACoefficientAtLayerInterfacesInitializer {
        
        private static final Map<String, Consumer<HybridACoefficientAtLayerInterfacesVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hybrid_a_coefficient_at_layer_interfaces >>
                // << customInit_hybrid_a_coefficient_at_layer_interfaces
            });
            // userInitializers_hybrid_a_coefficient_at_layer_interfaces >>
            // << userInitializers_hybrid_a_coefficient_at_layer_interfaces
        }

        public static HybridACoefficientAtLayerInterfacesVariable<double[]> initialize(HybridACoefficientAtLayerInterfacesVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HybridACoefficientAtLayerInterfacesVariable<double[]> initialize(String key, HybridACoefficientAtLayerInterfacesVariable<double[]> value) {
            if (value == null) {
                value = new HybridACoefficientAtLayerInterfacesVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class HybridBCoefficientAtLayerInterfacesInitializer {
        
        private static final Map<String, Consumer<HybridBCoefficientAtLayerInterfacesVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hybrid_b_coefficient_at_layer_interfaces >>
                // << customInit_hybrid_b_coefficient_at_layer_interfaces
            });
            // userInitializers_hybrid_b_coefficient_at_layer_interfaces >>
            // << userInitializers_hybrid_b_coefficient_at_layer_interfaces
        }

        public static HybridBCoefficientAtLayerInterfacesVariable<double[]> initialize(HybridBCoefficientAtLayerInterfacesVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HybridBCoefficientAtLayerInterfacesVariable<double[]> initialize(String key, HybridBCoefficientAtLayerInterfacesVariable<double[]> value) {
            if (value == null) {
                value = new HybridBCoefficientAtLayerInterfacesVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class HybridACoefficientAtLayerMidpointsInitializer {
        
        private static final Map<String, Consumer<HybridACoefficientAtLayerMidpointsVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hybrid_a_coefficient_at_layer_midpoints >>
                // << customInit_hybrid_a_coefficient_at_layer_midpoints
            });
            // userInitializers_hybrid_a_coefficient_at_layer_midpoints >>
            // << userInitializers_hybrid_a_coefficient_at_layer_midpoints
        }

        public static HybridACoefficientAtLayerMidpointsVariable<double[]> initialize(HybridACoefficientAtLayerMidpointsVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HybridACoefficientAtLayerMidpointsVariable<double[]> initialize(String key, HybridACoefficientAtLayerMidpointsVariable<double[]> value) {
            if (value == null) {
                value = new HybridACoefficientAtLayerMidpointsVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class HybridBCoefficientAtLayerMidpointsInitializer {
        
        private static final Map<String, Consumer<HybridBCoefficientAtLayerMidpointsVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_hybrid_b_coefficient_at_layer_midpoints >>
                // << customInit_hybrid_b_coefficient_at_layer_midpoints
            });
            // userInitializers_hybrid_b_coefficient_at_layer_midpoints >>
            // << userInitializers_hybrid_b_coefficient_at_layer_midpoints
        }

        public static HybridBCoefficientAtLayerMidpointsVariable<double[]> initialize(HybridBCoefficientAtLayerMidpointsVariable<double[]> value) {
            return initialize(null, value);
        }

        public static HybridBCoefficientAtLayerMidpointsVariable<double[]> initialize(String key, HybridBCoefficientAtLayerMidpointsVariable<double[]> value) {
            if (value == null) {
                value = new HybridBCoefficientAtLayerMidpointsVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class GaussWeightsInitializer {
        
        private static final Map<String, Consumer<GaussWeightsVariable<double[]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_gauss_weights >>
                // << customInit_gauss_weights
            });
            // userInitializers_gauss_weights >>
            // << userInitializers_gauss_weights
        }

        public static GaussWeightsVariable<double[]> initialize(GaussWeightsVariable<double[]> value) {
            return initialize(null, value);
        }

        public static GaussWeightsVariable<double[]> initialize(String key, GaussWeightsVariable<double[]> value) {
            if (value == null) {
                value = new GaussWeightsVO();
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
                // customInit_reference_pressure >>
                // << customInit_reference_pressure
            });
            // userInitializers_reference_pressure >>
            // << userInitializers_reference_pressure
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
    public static class SpectralTruncationParameterMInitializer {
        
        private static final Map<String, Consumer<SpectralTruncationParameterMVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("spectral truncation parameter M");
                }
                // customInit_spectral_truncation_parameter_m >>
                // << customInit_spectral_truncation_parameter_m
            });
            // userInitializers_spectral_truncation_parameter_m >>
            // << userInitializers_spectral_truncation_parameter_m
        }

        public static SpectralTruncationParameterMVariable<Integer> initialize(SpectralTruncationParameterMVariable<Integer> value) {
            return initialize(null, value);
        }

        public static SpectralTruncationParameterMVariable<Integer> initialize(String key, SpectralTruncationParameterMVariable<Integer> value) {
            if (value == null) {
                value = new SpectralTruncationParameterMVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SpectralTruncationParameterNInitializer {
        
        private static final Map<String, Consumer<SpectralTruncationParameterNVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("spectral truncation parameter N");
                }
                // customInit_spectral_truncation_parameter_n >>
                // << customInit_spectral_truncation_parameter_n
            });
            // userInitializers_spectral_truncation_parameter_n >>
            // << userInitializers_spectral_truncation_parameter_n
        }

        public static SpectralTruncationParameterNVariable<Integer> initialize(SpectralTruncationParameterNVariable<Integer> value) {
            return initialize(null, value);
        }

        public static SpectralTruncationParameterNVariable<Integer> initialize(String key, SpectralTruncationParameterNVariable<Integer> value) {
            if (value == null) {
                value = new SpectralTruncationParameterNVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SpectralTruncationParameterKInitializer {
        
        private static final Map<String, Consumer<SpectralTruncationParameterKVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("spectral truncation parameter K");
                }
                // customInit_spectral_truncation_parameter_k >>
                // << customInit_spectral_truncation_parameter_k
            });
            // userInitializers_spectral_truncation_parameter_k >>
            // << userInitializers_spectral_truncation_parameter_k
        }

        public static SpectralTruncationParameterKVariable<Integer> initialize(SpectralTruncationParameterKVariable<Integer> value) {
            return initialize(null, value);
        }

        public static SpectralTruncationParameterKVariable<Integer> initialize(String key, SpectralTruncationParameterKVariable<Integer> value) {
            if (value == null) {
                value = new SpectralTruncationParameterKVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class BaseDayInitializer {
        
        private static final Map<String, Consumer<BaseDayVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("base day");
                }
                // customInit_base_day >>
                // << customInit_base_day
            });
            // userInitializers_base_day >>
            // << userInitializers_base_day
        }

        public static BaseDayVariable<Integer> initialize(BaseDayVariable<Integer> value) {
            return initialize(null, value);
        }

        public static BaseDayVariable<Integer> initialize(String key, BaseDayVariable<Integer> value) {
            if (value == null) {
                value = new BaseDayVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SecondsOfBaseDayInitializer {
        
        private static final Map<String, Consumer<SecondsOfBaseDayVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("seconds of base day");
                }
                // customInit_seconds_of_base_day >>
                // << customInit_seconds_of_base_day
            });
            // userInitializers_seconds_of_base_day >>
            // << userInitializers_seconds_of_base_day
        }

        public static SecondsOfBaseDayVariable<Integer> initialize(SecondsOfBaseDayVariable<Integer> value) {
            return initialize(null, value);
        }

        public static SecondsOfBaseDayVariable<Integer> initialize(String key, SecondsOfBaseDayVariable<Integer> value) {
            if (value == null) {
                value = new SecondsOfBaseDayVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class BaseDateInitializer {
        
        private static final Map<String, Consumer<BaseDateVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("base date (YYYYMMDD)");
                }
                // customInit_base_date >>
                // << customInit_base_date
            });
            // userInitializers_base_date >>
            // << userInitializers_base_date
        }

        public static BaseDateVariable<Integer> initialize(BaseDateVariable<Integer> value) {
            return initialize(null, value);
        }

        public static BaseDateVariable<Integer> initialize(String key, BaseDateVariable<Integer> value) {
            if (value == null) {
                value = new BaseDateVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SecondsOfBaseDateInitializer {
        
        private static final Map<String, Consumer<SecondsOfBaseDateVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("seconds of base date");
                }
                // customInit_seconds_of_base_date >>
                // << customInit_seconds_of_base_date
            });
            // userInitializers_seconds_of_base_date >>
            // << userInitializers_seconds_of_base_date
        }

        public static SecondsOfBaseDateVariable<Integer> initialize(SecondsOfBaseDateVariable<Integer> value) {
            return initialize(null, value);
        }

        public static SecondsOfBaseDateVariable<Integer> initialize(String key, SecondsOfBaseDateVariable<Integer> value) {
            if (value == null) {
                value = new SecondsOfBaseDateVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class TimestepInitializer {
        
        private static final Map<String, Consumer<TimestepVariable<Integer>>> INITIALIZERS = new HashMap<>(); // false

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("timestep");
                }
                if (model.getUnits() == null) {
                    model.setUnits("s");
                }
                // customInit_timestep >>
                // << customInit_timestep
            });
            // userInitializers_timestep >>
            // << userInitializers_timestep
        }

        public static TimestepVariable<Integer> initialize(TimestepVariable<Integer> value) {
            return initialize(null, value);
        }

        public static TimestepVariable<Integer> initialize(String key, TimestepVariable<Integer> value) {
            if (value == null) {
                value = new TimestepVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class CurrentDayInitializer {
        
        private static final Map<String, Consumer<CurrentDayVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current day (from base day)");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_current_day >>
                // << customInit_current_day
            });
            // userInitializers_current_day >>
            // << userInitializers_current_day
        }

        public static CurrentDayVariable<int[]> initialize(CurrentDayVariable<int[]> value) {
            return initialize(null, value);
        }

        public static CurrentDayVariable<int[]> initialize(String key, CurrentDayVariable<int[]> value) {
            if (value == null) {
                value = new CurrentDayVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class CurrentSecondsOfCurrentDayInitializer {
        
        private static final Map<String, Consumer<CurrentSecondsOfCurrentDayVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current seconds of current day");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_current_seconds_of_current_day >>
                // << customInit_current_seconds_of_current_day
            });
            // userInitializers_current_seconds_of_current_day >>
            // << userInitializers_current_seconds_of_current_day
        }

        public static CurrentSecondsOfCurrentDayVariable<int[]> initialize(CurrentSecondsOfCurrentDayVariable<int[]> value) {
            return initialize(null, value);
        }

        public static CurrentSecondsOfCurrentDayVariable<int[]> initialize(String key, CurrentSecondsOfCurrentDayVariable<int[]> value) {
            if (value == null) {
                value = new CurrentSecondsOfCurrentDayVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class CurrentDateInitializer {
        
        private static final Map<String, Consumer<CurrentDateVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current date (YYYYMMDD)");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_current_date >>
                // << customInit_current_date
            });
            // userInitializers_current_date >>
            // << userInitializers_current_date
        }

        public static CurrentDateVariable<int[]> initialize(CurrentDateVariable<int[]> value) {
            return initialize(null, value);
        }

        public static CurrentDateVariable<int[]> initialize(String key, CurrentDateVariable<int[]> value) {
            if (value == null) {
                value = new CurrentDateVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class CurrentSecondsOfCurrentDateInitializer {
        
        private static final Map<String, Consumer<CurrentSecondsOfCurrentDateVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current seconds of current date");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_current_seconds_of_current_date >>
                // << customInit_current_seconds_of_current_date
            });
            // userInitializers_current_seconds_of_current_date >>
            // << userInitializers_current_seconds_of_current_date
        }

        public static CurrentSecondsOfCurrentDateVariable<int[]> initialize(CurrentSecondsOfCurrentDateVariable<int[]> value) {
            return initialize(null, value);
        }

        public static CurrentSecondsOfCurrentDateVariable<int[]> initialize(String key, CurrentSecondsOfCurrentDateVariable<int[]> value) {
            if (value == null) {
                value = new CurrentSecondsOfCurrentDateVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class CurrentTimestepInitializer {
        
        private static final Map<String, Consumer<CurrentTimestepVariable<int[]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getLongName() == null) {
                    model.setLongName("current timestep");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                }
                // customInit_current_timestep >>
                // << customInit_current_timestep
            });
            // userInitializers_current_timestep >>
            // << userInitializers_current_timestep
        }

        public static CurrentTimestepVariable<int[]> initialize(CurrentTimestepVariable<int[]> value) {
            return initialize(null, value);
        }

        public static CurrentTimestepVariable<int[]> initialize(String key, CurrentTimestepVariable<int[]> value) {
            if (value == null) {
                value = new CurrentTimestepVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class WindInitializer {
        
        private static final Map<String, Consumer<WindVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getUnits() == null) {
                    model.setUnits("m/s");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_wind >>
                // << customInit_wind
            });
            // userInitializers_wind >>
            // << userInitializers_wind
        }

        public static WindVariable<double[][][][]> initialize(WindVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static WindVariable<double[][][][]> initialize(String key, WindVariable<double[][][][]> value) {
            if (value == null) {
                value = new WindVO();
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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
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
    public static class SpecificHumidityInitializer {
        
        private static final Map<String, Consumer<SpecificHumidityVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_specific_humidity >>
                // << customInit_specific_humidity
            });
            // userInitializers_specific_humidity >>
            // << userInitializers_specific_humidity
        }

        public static SpecificHumidityVariable<double[][][][]> initialize(SpecificHumidityVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static SpecificHumidityVariable<double[][][][]> initialize(String key, SpecificHumidityVariable<double[][][][]> value) {
            if (value == null) {
                value = new SpecificHumidityVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SurfacePressureInitializer {
        
        private static final Map<String, Consumer<SurfacePressureVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_surface_pressure >>
                // << customInit_surface_pressure
            });
            // userInitializers_surface_pressure >>
            // << userInitializers_surface_pressure
        }

        public static SurfacePressureVariable<double[][][]> initialize(SurfacePressureVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static SurfacePressureVariable<double[][][]> initialize(String key, SurfacePressureVariable<double[][][]> value) {
            if (value == null) {
                value = new SurfacePressureVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SurfaceGeopotentialInitializer {
        
        private static final Map<String, Consumer<SurfaceGeopotentialVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_surface_geopotential >>
                // << customInit_surface_geopotential
            });
            // userInitializers_surface_geopotential >>
            // << userInitializers_surface_geopotential
        }

        public static SurfaceGeopotentialVariable<double[][][]> initialize(SurfaceGeopotentialVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static SurfaceGeopotentialVariable<double[][][]> initialize(String key, SurfaceGeopotentialVariable<double[][][]> value) {
            if (value == null) {
                value = new SurfaceGeopotentialVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class OrographyStandardDeviationInitializer {
        
        private static final Map<String, Consumer<OrographyStandardDeviationVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_orography_standard_deviation >>
                // << customInit_orography_standard_deviation
            });
            // userInitializers_orography_standard_deviation >>
            // << userInitializers_orography_standard_deviation
        }

        public static OrographyStandardDeviationVariable<double[][][]> initialize(OrographyStandardDeviationVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static OrographyStandardDeviationVariable<double[][][]> initialize(String key, OrographyStandardDeviationVariable<double[][][]> value) {
            if (value == null) {
                value = new OrographyStandardDeviationVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class LandOceanTransitionMaskInitializer {
        
        private static final Map<String, Consumer<LandOceanTransitionMaskVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_land_ocean_transition_mask >>
                // << customInit_land_ocean_transition_mask
            });
            // userInitializers_land_ocean_transition_mask >>
            // << userInitializers_land_ocean_transition_mask
        }

        public static LandOceanTransitionMaskVariable<double[][][]> initialize(LandOceanTransitionMaskVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static LandOceanTransitionMaskVariable<double[][][]> initialize(String key, LandOceanTransitionMaskVariable<double[][][]> value) {
            if (value == null) {
                value = new LandOceanTransitionMaskVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class PblHeightInitializer {
        
        private static final Map<String, Consumer<PblHeightVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_pbl_height >>
                // << customInit_pbl_height
            });
            // userInitializers_pbl_height >>
            // << userInitializers_pbl_height
        }

        public static PblHeightVariable<double[][][]> initialize(PblHeightVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static PblHeightVariable<double[][][]> initialize(String key, PblHeightVariable<double[][][]> value) {
            if (value == null) {
                value = new PblHeightVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class PerturbationTemperatureInitializer {
        
        private static final Map<String, Consumer<PerturbationTemperatureVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_perturbation_temperature >>
                // << customInit_perturbation_temperature
            });
            // userInitializers_perturbation_temperature >>
            // << userInitializers_perturbation_temperature
        }

        public static PerturbationTemperatureVariable<double[][][]> initialize(PerturbationTemperatureVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static PerturbationTemperatureVariable<double[][][]> initialize(String key, PerturbationTemperatureVariable<double[][][]> value) {
            if (value == null) {
                value = new PerturbationTemperatureVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class PerturbationSpecificHumidityInitializer {
        
        private static final Map<String, Consumer<PerturbationSpecificHumidityVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_perturbation_specific_humidity >>
                // << customInit_perturbation_specific_humidity
            });
            // userInitializers_perturbation_specific_humidity >>
            // << userInitializers_perturbation_specific_humidity
        }

        public static PerturbationSpecificHumidityVariable<double[][][]> initialize(PerturbationSpecificHumidityVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static PerturbationSpecificHumidityVariable<double[][][]> initialize(String key, PerturbationSpecificHumidityVariable<double[][][]> value) {
            if (value == null) {
                value = new PerturbationSpecificHumidityVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class CloudFractionInitializer {
        
        private static final Map<String, Consumer<CloudFractionVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_cloud_fraction >>
                // << customInit_cloud_fraction
            });
            // userInitializers_cloud_fraction >>
            // << userInitializers_cloud_fraction
        }

        public static CloudFractionVariable<double[][][][]> initialize(CloudFractionVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static CloudFractionVariable<double[][][][]> initialize(String key, CloudFractionVariable<double[][][][]> value) {
            if (value == null) {
                value = new CloudFractionVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SecondaryTemperatureInitializer {
        
        private static final Map<String, Consumer<SecondaryTemperatureVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getUnits() == null) {
                    model.setUnits("K");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_secondary_temperature >>
                // << customInit_secondary_temperature
            });
            // userInitializers_secondary_temperature >>
            // << userInitializers_secondary_temperature
        }

        public static SecondaryTemperatureVariable<double[][][]> initialize(SecondaryTemperatureVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static SecondaryTemperatureVariable<double[][][]> initialize(String key, SecondaryTemperatureVariable<double[][][]> value) {
            if (value == null) {
                value = new SecondaryTemperatureVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class WaterEquivalentSnowDepthInitializer {
        
        private static final Map<String, Consumer<WaterEquivalentSnowDepthVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_water_equivalent_snow_depth >>
                // << customInit_water_equivalent_snow_depth
            });
            // userInitializers_water_equivalent_snow_depth >>
            // << userInitializers_water_equivalent_snow_depth
        }

        public static WaterEquivalentSnowDepthVariable<double[][][]> initialize(WaterEquivalentSnowDepthVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static WaterEquivalentSnowDepthVariable<double[][][]> initialize(String key, WaterEquivalentSnowDepthVariable<double[][][]> value) {
            if (value == null) {
                value = new WaterEquivalentSnowDepthVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class GridboxLandFractionInitializer {
        
        private static final Map<String, Consumer<GridboxLandFractionVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_gridbox_land_fraction >>
                // << customInit_gridbox_land_fraction
            });
            // userInitializers_gridbox_land_fraction >>
            // << userInitializers_gridbox_land_fraction
        }

        public static GridboxLandFractionVariable<double[][][]> initialize(GridboxLandFractionVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static GridboxLandFractionVariable<double[][][]> initialize(String key, GridboxLandFractionVariable<double[][][]> value) {
            if (value == null) {
                value = new GridboxLandFractionVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class FractionOfSfcAreaCoveredBySeaIceInitializer {
        
        private static final Map<String, Consumer<FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_fraction_of_sfc_area_covered_by_sea_ice >>
                // << customInit_fraction_of_sfc_area_covered_by_sea_ice
            });
            // userInitializers_fraction_of_sfc_area_covered_by_sea_ice >>
            // << userInitializers_fraction_of_sfc_area_covered_by_sea_ice
        }

        public static FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> initialize(FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> initialize(String key, FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> value) {
            if (value == null) {
                value = new FractionOfSfcAreaCoveredBySeaIceVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class SeaIceThicknessInitializer {
        
        private static final Map<String, Consumer<SeaIceThicknessVariable<double[][][]>>> INITIALIZERS = new HashMap<>(); // true

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
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                }
                // customInit_sea_ice_thickness >>
                // << customInit_sea_ice_thickness
            });
            // userInitializers_sea_ice_thickness >>
            // << userInitializers_sea_ice_thickness
        }

        public static SeaIceThicknessVariable<double[][][]> initialize(SeaIceThicknessVariable<double[][][]> value) {
            return initialize(null, value);
        }

        public static SeaIceThicknessVariable<double[][][]> initialize(String key, SeaIceThicknessVariable<double[][][]> value) {
            if (value == null) {
                value = new SeaIceThicknessVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
    public static class GridBoxAveragedCondensateAmountInitializer {
        
        private static final Map<String, Consumer<GridBoxAveragedCondensateAmountVariable<double[][][][]>>> INITIALIZERS = new HashMap<>(); // true

        static {
            INITIALIZERS.put(null, model -> {
                if (model.getUnits() == null) {
                    model.setUnits("kg/kg");
                }
                if (model.getValue() != null && model.getDimensions() == null) {
                    model.setDimensions(new ArrayList<>());
                    model.getDimensions().add(Dimension.builder()
                            .setName("time")
                            .setLength(ArrayUtils.getLength(model.getValue(), 0))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lat")
                            .setLength(ArrayUtils.getLength(model.getValue(), 1))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lev")
                            .setLength(ArrayUtils.getLength(model.getValue(), 2))
                            .build());
                    model.getDimensions().add(Dimension.builder()
                            .setName("lon")
                            .setLength(ArrayUtils.getLength(model.getValue(), 3))
                            .build());
                }
                // customInit_grid_box_averaged_condensate_amount >>
                // << customInit_grid_box_averaged_condensate_amount
            });
            // userInitializers_grid_box_averaged_condensate_amount >>
            // << userInitializers_grid_box_averaged_condensate_amount
        }

        public static GridBoxAveragedCondensateAmountVariable<double[][][][]> initialize(GridBoxAveragedCondensateAmountVariable<double[][][][]> value) {
            return initialize(null, value);
        }

        public static GridBoxAveragedCondensateAmountVariable<double[][][][]> initialize(String key, GridBoxAveragedCondensateAmountVariable<double[][][][]> value) {
            if (value == null) {
                value = new GridBoxAveragedCondensateAmountVO();
            }
            INITIALIZERS.get(key).accept(value);
            return value;
        }

    }
}
