package io.github.hlfsousa.ncml.io.cami;

/*-
 * #%L
 * ncml-io
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

// imports >>
import io.github.hlfsousa.ncml.annotation.*;
import io.github.hlfsousa.ncml.declaration.*;
import java.util.*;
import ucar.ma2.Array;
// << imports

@CDLRoot
@CDLDimensions({
        @CDLDimension(name = "lat", length = 64),
        @CDLDimension(name = "lon", length = 128),
        @CDLDimension(name = "lev", length = 26),
        @CDLDimension(name = "ilev", length = 27),
        @CDLDimension(name = "time", length = 1, unlimited = true),
        @CDLDimension(name = "chars", length = 8)
})
public interface CommunityAtmosphericModel {
    interface LatitudeVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "latitude")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "degrees_north")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface LongitudeVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "longitude")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "degrees_east")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface HybridLevelVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false)
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "level")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "positive", dataType = "String", unsigned = false, defaultValue = "down")
        String getPositive();

        void setPositive(String positive);

        @CDLAttribute(name = "standard_name", dataType = "String", unsigned = false, defaultValue = "atmosphere_hybrid_sigma_pressure_coordinate")
        String getStandardName();

        void setStandardName(String standardName);

        @CDLAttribute(name = "formula_terms", dataType = "String", unsigned = false)
        String getFormulaTerms();

        void setFormulaTerms(String formulaTerms);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface TimeVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "time")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "days since 0000-08-29 00:00:00")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "calendar", dataType = "String", unsigned = false, defaultValue = "noleap")
        String getCalendar();

        void setCalendar(String calendar);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface HybridACoefficientAtLayerInterfacesVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "hybrid A coefficient at layer interfaces")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface HybridBCoefficientAtLayerInterfacesVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "hybrid B coefficient at layer interfaces")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface HybridACoefficientAtLayerMidpointsVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "hybrid A coefficient at layer midpoints")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface HybridBCoefficientAtLayerMidpointsVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "hybrid B coefficient at layer midpoints")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface GaussWeightsVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "gauss weights")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "9.99999961690316e+35")
        Double getFillValue();

        void setFillValue(Double fillValue);

    }

    interface ReferencePressureVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "reference pressure")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "Pa")
        String getUnits();

        void setUnits(String units);

    }

    interface DateWrittenVariable<T> extends Variable<T> {

    }

    interface TimeWrittenVariable<T> extends Variable<T> {

    }

    interface SpectralTruncationParameterMVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "spectral truncation parameter M")
        String getLongName();

        void setLongName(String longName);

    }

    interface SpectralTruncationParameterNVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "spectral truncation parameter N")
        String getLongName();

        void setLongName(String longName);

    }

    interface SpectralTruncationParameterKVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "spectral truncation parameter K")
        String getLongName();

        void setLongName(String longName);

    }

    interface BaseDayVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "base day")
        String getLongName();

        void setLongName(String longName);

    }

    interface SecondsOfBaseDayVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "seconds of base day")
        String getLongName();

        void setLongName(String longName);

    }

    interface BaseDateVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "base date (YYYYMMDD)")
        String getLongName();

        void setLongName(String longName);

    }

    interface SecondsOfBaseDateVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "seconds of base date")
        String getLongName();

        void setLongName(String longName);

    }

    interface TimestepVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "timestep")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "s")
        String getUnits();

        void setUnits(String units);

    }

    interface CurrentDayVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "current day (from base day)")
        String getLongName();

        void setLongName(String longName);

    }

    interface CurrentSecondsOfCurrentDayVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "current seconds of current day")
        String getLongName();

        void setLongName(String longName);

    }

    interface CurrentDateVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "current date (YYYYMMDD)")
        String getLongName();

        void setLongName(String longName);

    }

    interface CurrentSecondsOfCurrentDateVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "current seconds of current date")
        String getLongName();

        void setLongName(String longName);

    }

    interface CurrentTimestepVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "current timestep")
        String getLongName();

        void setLongName(String longName);

    }

    interface WindVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false)
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "m/s")
        String getUnits();

        void setUnits(String units);

    }

    interface TemperatureVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Temperature")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "K")
        String getUnits();

        void setUnits(String units);

    }

    interface SpecificHumidityVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Specific humidity")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "kg/kg")
        String getUnits();

        void setUnits(String units);

    }

    interface SurfacePressureVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Surface pressure")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "Pa")
        String getUnits();

        void setUnits(String units);

    }

    interface SurfaceGeopotentialVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "surface geopotential")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "M2/S2")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "1.e+36")
        Double getFillValue();

        void setFillValue(Double fillValue);

        @CDLAttribute(name = "from_hires", dataType = "String", unsigned = false, defaultValue = "true")
        String getFromHires();

        void setFromHires(String fromHires);

    }

    interface OrographyStandardDeviationVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "orography standard deviation")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "M")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "1.e+36")
        Double getFillValue();

        void setFillValue(Double fillValue);

        @CDLAttribute(name = "from_hires", dataType = "String", unsigned = false, defaultValue = "true")
        String getFromHires();

        void setFromHires(String fromHires);

    }

    interface LandOceanTransitionMaskVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "land ocean transition mask: ocean (0), continent (1), transition (0-1)")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "none")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "1.e+36")
        Double getFillValue();

        void setFillValue(Double fillValue);

        @CDLAttribute(name = "from_hires", dataType = "String", unsigned = false, defaultValue = "true")
        String getFromHires();

        void setFromHires(String fromHires);

    }

    interface PblHeightVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "PBL height")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "m")
        String getUnits();

        void setUnits(String units);

    }

    interface PerturbationTemperatureVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Perturbation temperature (eddies in PBL)")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "K")
        String getUnits();

        void setUnits(String units);

    }

    interface PerturbationSpecificHumidityVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Perturbation specific humidity (eddies in PBL)")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "kg/kg")
        String getUnits();

        void setUnits(String units);

    }

    interface CloudFractionVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Cloud fraction")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "fraction")
        String getUnits();

        void setUnits(String units);

    }

    interface QCWATVariable<T> extends Variable<T> {

    }

    interface TCWATVariable<T> extends Variable<T> {

    }

    interface LCWATVariable<T> extends Variable<T> {

    }

    interface TSICERADVariable<T> extends Variable<T> {

    }

    interface SecondaryTemperatureVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false)
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "K")
        String getUnits();

        void setUnits(String units);

    }

    interface WaterEquivalentSnowDepthVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Water equivalent snow depth")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "m")
        String getUnits();

        void setUnits(String units);

    }

    interface GridboxLandFractionVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "gridbox land fraction")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "FRAC")
        String getUnits();

        void setUnits(String units);

        @CDLAttribute(name = "_FillValue", dataType = "double", unsigned = false, defaultValue = "1.e+36")
        Double getFillValue();

        void setFillValue(Double fillValue);

        @CDLAttribute(name = "from_hires", dataType = "String", unsigned = false, defaultValue = "true")
        String getFromHires();

        void setFromHires(String fromHires);

    }

    interface FractionOfSfcAreaCoveredBySeaIceVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Fraction of sfc area covered by sea-ice")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "fraction")
        String getUnits();

        void setUnits(String units);

    }

    interface SeaIceThicknessVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false, defaultValue = "Sea ice thickness")
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "m")
        String getUnits();

        void setUnits(String units);

    }

    interface TSOCNVariable<T> extends Variable<T> {

    }

    interface GridBoxAveragedCondensateAmountVariable<T> extends Variable<T> {

        @CDLAttribute(name = "long_name", dataType = "String", unsigned = false)
        String getLongName();

        void setLongName(String longName);

        @CDLAttribute(name = "units", dataType = "String", unsigned = false, defaultValue = "kg/kg")
        String getUnits();

        void setUnits(String units);

    }

    @CDLAttribute(name = "Conventions", dataType = "String", unsigned = false, defaultValue = "CF-1.0")
    String getConventions();

    void setConventions(String conventions);

    @CDLAttribute(name = "logname", dataType = "String", unsigned = false, defaultValue = "olson")
    String getLogname();

    void setLogname(String logname);

    @CDLAttribute(name = "host", dataType = "String", unsigned = false, defaultValue = "bb0001en")
    String getHost();

    void setHost(String host);

    @CDLAttribute(name = "source", dataType = "String", unsigned = false, defaultValue = "Interpolated from:/fs/cgd/data0/olson/inputIC/newICeul.cam2.i.0000-09-01-00000.nc::CAM")
    String getSource();

    void setSource(String source);

    @CDLAttribute(name = "case", dataType = "String", unsigned = false, defaultValue = "cam2run")
    String getCase();

    void setCase(String _case);

    @CDLAttribute(name = "title", dataType = "String", unsigned = false, defaultValue = "Interpolated from:/fs/cgd/data0/olson/inputIC/newICeul.cam2.i.0000-09-01-00000.nc::atm ver atm, eul ver v013, case newICeul")
    String getTitle();

    void setTitle(String title);

    @CDLAttribute(name = "history", dataType = "String", unsigned = false, defaultValue = "\n05/07/03 12:15:34 olson:chinookfe:interpic -t SEP1.T42L26.gaussian.template.nc /fs/cgd/data0/olson/inputIC/newICeul.cam2.i.0000-09-01-00000.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam1/hrtopo/topo.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo-usgs-10min.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo10min.merged_c030506.nc cami_0000-09-01_64x128_L26_c030507.nc\ndefinesurf -t /fs/cgd/csm/inputdata/atm/cam2/hrtopo/topo10min.merged_c030506.nc -l cami_0000-09-01_64x128_L26_c030624.nc.new")
    String getHistory();

    void setHistory(String history);

    @CDLAttribute(name = "make_ross", dataType = "String", unsigned = false, defaultValue = "true")
    String getMakeRoss();

    void setMakeRoss(String makeRoss);

    @CDLVariable(name = "lat", dataType = "double", unsigned = false, shape = { "lat" })
    LatitudeVariable<double[]> getLatitude();

    void setLatitude(LatitudeVariable<double[]> latitude);

    @CDLVariable(name = "lon", dataType = "double", unsigned = false, shape = { "lon" })
    LongitudeVariable<double[]> getLongitude();

    void setLongitude(LongitudeVariable<double[]> longitude);

    @CDLVariable(name = "hybrid_level:lev|ilev", dataType = "double", unsigned = false, shape = { "lev" })
    Map<String, HybridLevelVariable<double[]>> getHybridLevel();

    void setHybridLevel(Map<String, HybridLevelVariable<double[]>> hybridLevel);

    @CDLVariable(name = "time", dataType = "double", unsigned = false, shape = { "time" })
    TimeVariable<double[]> getTime();

    void setTime(TimeVariable<double[]> time);

    @CDLVariable(name = "hyai", dataType = "double", unsigned = false, shape = { "ilev" })
    HybridACoefficientAtLayerInterfacesVariable<double[]> getHybridACoefficientAtLayerInterfaces();

    void setHybridACoefficientAtLayerInterfaces(HybridACoefficientAtLayerInterfacesVariable<double[]> hybridACoefficientAtLayerInterfaces);

    @CDLVariable(name = "hybi", dataType = "double", unsigned = false, shape = { "ilev" })
    HybridBCoefficientAtLayerInterfacesVariable<double[]> getHybridBCoefficientAtLayerInterfaces();

    void setHybridBCoefficientAtLayerInterfaces(HybridBCoefficientAtLayerInterfacesVariable<double[]> hybridBCoefficientAtLayerInterfaces);

    @CDLVariable(name = "hyam", dataType = "double", unsigned = false, shape = { "lev" })
    HybridACoefficientAtLayerMidpointsVariable<double[]> getHybridACoefficientAtLayerMidpoints();

    void setHybridACoefficientAtLayerMidpoints(HybridACoefficientAtLayerMidpointsVariable<double[]> hybridACoefficientAtLayerMidpoints);

    @CDLVariable(name = "hybm", dataType = "double", unsigned = false, shape = { "lev" })
    HybridBCoefficientAtLayerMidpointsVariable<double[]> getHybridBCoefficientAtLayerMidpoints();

    void setHybridBCoefficientAtLayerMidpoints(HybridBCoefficientAtLayerMidpointsVariable<double[]> hybridBCoefficientAtLayerMidpoints);

    @CDLVariable(name = "gw", dataType = "double", unsigned = false, shape = { "lat" })
    GaussWeightsVariable<double[]> getGaussWeights();

    void setGaussWeights(GaussWeightsVariable<double[]> gaussWeights);

    @CDLVariable(name = "P0", dataType = "double", unsigned = false)
    ReferencePressureVariable<Double> getReferencePressure();

    void setReferencePressure(ReferencePressureVariable<Double> referencePressure);

    @CDLVariable(name = "date_written", dataType = "char", unsigned = false, shape = { "time", "chars" })
    DateWrittenVariable<char[][]> getDateWritten();

    void setDateWritten(DateWrittenVariable<char[][]> dateWritten);

    @CDLVariable(name = "time_written", dataType = "char", unsigned = false, shape = { "time", "chars" })
    TimeWrittenVariable<char[][]> getTimeWritten();

    void setTimeWritten(TimeWrittenVariable<char[][]> timeWritten);

    @CDLVariable(name = "ntrm", dataType = "int", unsigned = false)
    SpectralTruncationParameterMVariable<Integer> getSpectralTruncationParameterM();

    void setSpectralTruncationParameterM(SpectralTruncationParameterMVariable<Integer> spectralTruncationParameterM);

    @CDLVariable(name = "ntrn", dataType = "int", unsigned = false)
    SpectralTruncationParameterNVariable<Integer> getSpectralTruncationParameterN();

    void setSpectralTruncationParameterN(SpectralTruncationParameterNVariable<Integer> spectralTruncationParameterN);

    @CDLVariable(name = "ntrk", dataType = "int", unsigned = false)
    SpectralTruncationParameterKVariable<Integer> getSpectralTruncationParameterK();

    void setSpectralTruncationParameterK(SpectralTruncationParameterKVariable<Integer> spectralTruncationParameterK);

    @CDLVariable(name = "ndbase", dataType = "int", unsigned = false)
    BaseDayVariable<Integer> getBaseDay();

    void setBaseDay(BaseDayVariable<Integer> baseDay);

    @CDLVariable(name = "nsbase", dataType = "int", unsigned = false)
    SecondsOfBaseDayVariable<Integer> getSecondsOfBaseDay();

    void setSecondsOfBaseDay(SecondsOfBaseDayVariable<Integer> secondsOfBaseDay);

    @CDLVariable(name = "nbdate", dataType = "int", unsigned = false)
    BaseDateVariable<Integer> getBaseDate();

    void setBaseDate(BaseDateVariable<Integer> baseDate);

    @CDLVariable(name = "nbsec", dataType = "int", unsigned = false)
    SecondsOfBaseDateVariable<Integer> getSecondsOfBaseDate();

    void setSecondsOfBaseDate(SecondsOfBaseDateVariable<Integer> secondsOfBaseDate);

    @CDLVariable(name = "mdt", dataType = "int", unsigned = false)
    TimestepVariable<Integer> getTimestep();

    void setTimestep(TimestepVariable<Integer> timestep);

    @CDLVariable(name = "ndcur", dataType = "int", unsigned = false, shape = { "time" })
    CurrentDayVariable<int[]> getCurrentDay();

    void setCurrentDay(CurrentDayVariable<int[]> currentDay);

    @CDLVariable(name = "nscur", dataType = "int", unsigned = false, shape = { "time" })
    CurrentSecondsOfCurrentDayVariable<int[]> getCurrentSecondsOfCurrentDay();

    void setCurrentSecondsOfCurrentDay(CurrentSecondsOfCurrentDayVariable<int[]> currentSecondsOfCurrentDay);

    @CDLVariable(name = "date", dataType = "int", unsigned = false, shape = { "time" })
    CurrentDateVariable<int[]> getCurrentDate();

    void setCurrentDate(CurrentDateVariable<int[]> currentDate);

    @CDLVariable(name = "datesec", dataType = "int", unsigned = false, shape = { "time" })
    CurrentSecondsOfCurrentDateVariable<int[]> getCurrentSecondsOfCurrentDate();

    void setCurrentSecondsOfCurrentDate(CurrentSecondsOfCurrentDateVariable<int[]> currentSecondsOfCurrentDate);

    @CDLVariable(name = "nsteph", dataType = "int", unsigned = false, shape = { "time" })
    CurrentTimestepVariable<int[]> getCurrentTimestep();

    void setCurrentTimestep(CurrentTimestepVariable<int[]> currentTimestep);

    @CDLVariable(name = "wind:U|V", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    Map<String, WindVariable<double[][][][]>> getWind();

    void setWind(Map<String, WindVariable<double[][][][]>> wind);

    @CDLVariable(name = "T", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    TemperatureVariable<double[][][][]> getTemperature();

    void setTemperature(TemperatureVariable<double[][][][]> temperature);

    @CDLVariable(name = "Q", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    SpecificHumidityVariable<double[][][][]> getSpecificHumidity();

    void setSpecificHumidity(SpecificHumidityVariable<double[][][][]> specificHumidity);

    @CDLVariable(name = "PS", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    SurfacePressureVariable<double[][][]> getSurfacePressure();

    void setSurfacePressure(SurfacePressureVariable<double[][][]> surfacePressure);

    @CDLVariable(name = "PHIS", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    SurfaceGeopotentialVariable<double[][][]> getSurfaceGeopotential();

    void setSurfaceGeopotential(SurfaceGeopotentialVariable<double[][][]> surfaceGeopotential);

    @CDLVariable(name = "SGH", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    OrographyStandardDeviationVariable<double[][][]> getOrographyStandardDeviation();

    void setOrographyStandardDeviation(OrographyStandardDeviationVariable<double[][][]> orographyStandardDeviation);

    @CDLVariable(name = "land_ocean_transition_mask:LANDM|LANDM_COSLAT", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    Map<String, LandOceanTransitionMaskVariable<double[][][]>> getLandOceanTransitionMask();

    void setLandOceanTransitionMask(Map<String, LandOceanTransitionMaskVariable<double[][][]>> landOceanTransitionMask);

    @CDLVariable(name = "PBLH", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    PblHeightVariable<double[][][]> getPblHeight();

    void setPblHeight(PblHeightVariable<double[][][]> pblHeight);

    @CDLVariable(name = "TPERT", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    PerturbationTemperatureVariable<double[][][]> getPerturbationTemperature();

    void setPerturbationTemperature(PerturbationTemperatureVariable<double[][][]> perturbationTemperature);

    @CDLVariable(name = "QPERT", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    PerturbationSpecificHumidityVariable<double[][][]> getPerturbationSpecificHumidity();

    void setPerturbationSpecificHumidity(PerturbationSpecificHumidityVariable<double[][][]> perturbationSpecificHumidity);

    @CDLVariable(name = "CLOUD", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    CloudFractionVariable<double[][][][]> getCloudFraction();

    void setCloudFraction(CloudFractionVariable<double[][][][]> cloudFraction);

    @CDLVariable(name = "QCWAT", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    QCWATVariable<double[][][][]> getQCWAT();

    void setQCWAT(QCWATVariable<double[][][][]> qCWAT);

    @CDLVariable(name = "TCWAT", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    TCWATVariable<double[][][][]> getTCWAT();

    void setTCWAT(TCWATVariable<double[][][][]> tCWAT);

    @CDLVariable(name = "LCWAT", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    LCWATVariable<double[][][][]> getLCWAT();

    void setLCWAT(LCWATVariable<double[][][][]> lCWAT);

    @CDLVariable(name = "TSICERAD", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    TSICERADVariable<double[][][]> getTSICERAD();

    void setTSICERAD(TSICERADVariable<double[][][]> tSICERAD);

    @CDLVariable(name = "secondary_temperature:TS|TSICE|TS1|TS2|TS3|TS4|TBOT", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    Map<String, SecondaryTemperatureVariable<double[][][]>> getSecondaryTemperature();

    void setSecondaryTemperature(Map<String, SecondaryTemperatureVariable<double[][][]>> secondaryTemperature);

    @CDLVariable(name = "SNOWHICE", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    WaterEquivalentSnowDepthVariable<double[][][]> getWaterEquivalentSnowDepth();

    void setWaterEquivalentSnowDepth(WaterEquivalentSnowDepthVariable<double[][][]> waterEquivalentSnowDepth);

    @CDLVariable(name = "LANDFRAC", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    GridboxLandFractionVariable<double[][][]> getGridboxLandFraction();

    void setGridboxLandFraction(GridboxLandFractionVariable<double[][][]> gridboxLandFraction);

    @CDLVariable(name = "ICEFRAC", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> getFractionOfSfcAreaCoveredBySeaIce();

    void setFractionOfSfcAreaCoveredBySeaIce(FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> fractionOfSfcAreaCoveredBySeaIce);

    @CDLVariable(name = "SICTHK", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    SeaIceThicknessVariable<double[][][]> getSeaIceThickness();

    void setSeaIceThickness(SeaIceThicknessVariable<double[][][]> seaIceThickness);

    @CDLVariable(name = "TSOCN", dataType = "double", unsigned = false, shape = { "time", "lat", "lon" })
    TSOCNVariable<double[][][]> getTSOCN();

    void setTSOCN(TSOCNVariable<double[][][]> tSOCN);

    @CDLVariable(name = "grid_box_averaged_condensate_amount:CLDLIQ|CLDICE", dataType = "double", unsigned = false, shape = { "time", "lat", "lev", "lon" })
    Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> getGridBoxAveragedCondensateAmount();

    void setGridBoxAveragedCondensateAmount(Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> gridBoxAveragedCondensateAmount);

    // methods >>
    public static void main(String[] args) {
        System.out.println(SeaIceThicknessVariable.class.getName());
    }
    // << methods

}
