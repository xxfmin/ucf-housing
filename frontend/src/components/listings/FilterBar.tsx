"use client";

import {
  bathroomOptions,
  bedroomOptions,
  cities,
  defaultFilters,
  FilterState,
  propertyTypes,
  zipCodes,
} from "@/types/Filter";
import { useState, useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { useIsMobile } from "@/hooks/use-mobile";
import { Card, CardContent } from "../ui/card";
import { Button } from "../ui/button";
import { CalendarIcon, Search, RotateCcw, ChevronDown } from "lucide-react";
import { Badge } from "../ui/badge";
import { Label } from "../ui/label";
import { Slider } from "../ui/slider";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { Calendar } from "../ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover";
import { format } from "date-fns";

export function FilterBar() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const isMobile = useIsMobile();

  const [localFilters, setLocalFilters] = useState<FilterState>(defaultFilters);

  // Parse URL search params on mount
  useEffect(() => {
    const urlFilters: FilterState = {
      priceRange: [
        parseInt(searchParams.get("minPrice") || "600"),
        parseInt(searchParams.get("maxPrice") || "15000"),
      ],
      areaRange: [
        parseInt(searchParams.get("minArea") || "150"),
        parseInt(searchParams.get("maxArea") || "5100"),
      ],
      cities: searchParams.getAll("cities"),
      zipCodes: searchParams.getAll("zipCodes"),
      bedrooms: searchParams.get("beds") || "any",
      bathrooms: searchParams.get("baths") || "any",
      propertyTypes: searchParams.getAll("propertyTypes"),
      availabilityDate: searchParams.get("availableBy")
        ? new Date(searchParams.get("availableBy")!)
        : undefined,
    };
    setLocalFilters(urlFilters);
  }, [searchParams]);

  const updateLocalFilters = (updates: Partial<FilterState>) => {
    setLocalFilters((prev) => ({ ...prev, ...updates }));
  };

  const toggleCity = (cityValue: string) => {
    const newCities = localFilters.cities.includes(cityValue)
      ? localFilters.cities.filter((c) => c !== cityValue)
      : [...localFilters.cities, cityValue];
    updateLocalFilters({ cities: newCities });
  };

  const toggleZipCode = (zipCode: string) => {
    const newZipCodes = localFilters.zipCodes.includes(zipCode)
      ? localFilters.zipCodes.filter((z) => z !== zipCode)
      : [...localFilters.zipCodes, zipCode];
    updateLocalFilters({ zipCodes: newZipCodes });
  };

  const togglePropertyType = (typeId: string) => {
    const newTypes = localFilters.propertyTypes.includes(typeId)
      ? localFilters.propertyTypes.filter((t) => t !== typeId)
      : [...localFilters.propertyTypes, typeId];
    updateLocalFilters({ propertyTypes: newTypes });
  };

  const resetFilters = () => {
    setLocalFilters(defaultFilters);
  };

  const applyFilters = () => {
    const params = new URLSearchParams();

    // Price range
    if (localFilters.priceRange[0] !== 600) {
      params.set("minPrice", localFilters.priceRange[0].toString());
    }
    if (localFilters.priceRange[1] !== 15000) {
      params.set("maxPrice", localFilters.priceRange[1].toString());
    }

    // Area range
    if (localFilters.areaRange[0] !== 150) {
      params.set("minArea", localFilters.areaRange[0].toString());
    }
    if (localFilters.areaRange[1] !== 5100) {
      params.set("maxArea", localFilters.areaRange[1].toString());
    }

    // Cities
    localFilters.cities.forEach((city) => params.append("cities", city));

    // Zip codes
    localFilters.zipCodes.forEach((zip) => params.append("zipCodes", zip));

    // Property types
    localFilters.propertyTypes.forEach((type) =>
      params.append("propertyTypes", type)
    );

    // Bedrooms/Bathrooms
    if (localFilters.bedrooms !== "any") {
      params.set("beds", localFilters.bedrooms);
    }
    if (localFilters.bathrooms !== "any") {
      params.set("baths", localFilters.bathrooms);
    }

    // Availability date
    if (localFilters.availabilityDate) {
      params.set(
        "availableBy",
        localFilters.availabilityDate.toISOString().split("T")[0]
      );
    }

    const queryString = params.toString();
    router.push(queryString ? `/?${queryString}` : "/");
  };

  const getActiveFilterCount = () => {
    let count = 0;
    if (
      localFilters.priceRange[0] !== 600 ||
      localFilters.priceRange[1] !== 15000
    )
      count++;
    if (localFilters.areaRange[0] !== 150 || localFilters.areaRange[1] !== 5100)
      count++;
    if (localFilters.cities.length > 0) count++;
    if (localFilters.zipCodes.length > 0) count++;
    if (localFilters.propertyTypes.length > 0) count++;
    if (localFilters.bedrooms !== "any") count++;
    if (localFilters.bathrooms !== "any") count++;
    if (localFilters.availabilityDate) count++;
    return count;
  };

  return (
    <Card
      className={`pt-4 mb-6 border-0 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 ${
        !isMobile ? "sticky top-0 z-10" : ""
      }`}
    >
      <CardContent className="px-6">
        {/* Mobile Layout */}
        <div className="md:hidden space-y-4">
          {/* Price Range */}
          <div>
            <Label className="text-sm font-medium">Price Range</Label>
            <div className="mt-2">
              <Slider
                value={localFilters.priceRange}
                onValueChange={(value) =>
                  updateLocalFilters({ priceRange: value as [number, number] })
                }
                min={600}
                max={15000}
                step={50}
                className="w-full cursor-pointer"
              />
              <div className="flex justify-between mt-1 text-sm text-muted-foreground">
                <span>${localFilters.priceRange[0].toLocaleString()}</span>
                <span>${localFilters.priceRange[1].toLocaleString()}</span>
              </div>
            </div>
          </div>

          {/* Area Range */}
          <div>
            <Label className="text-sm font-medium">Area Range</Label>
            <div className="mt-2">
              <Slider
                value={localFilters.areaRange}
                onValueChange={(value) =>
                  updateLocalFilters({ areaRange: value as [number, number] })
                }
                min={150}
                max={5100}
                step={50}
                className="w-full cursor-pointer"
              />
              <div className="flex justify-between mt-1 text-sm text-muted-foreground">
                <span>{localFilters.areaRange[0].toLocaleString()} sq ft</span>
                <span>{localFilters.areaRange[1].toLocaleString()} sq ft</span>
              </div>
            </div>
          </div>

          {/* Mobile Dropdowns */}
          <div className="grid grid-cols-2 gap-4">
            {/* Bedrooms */}
            <div>
              <Label className="text-sm font-medium">Beds</Label>
              <Select
                value={localFilters.bedrooms}
                onValueChange={(value) =>
                  updateLocalFilters({ bedrooms: value })
                }
              >
                <SelectTrigger className="mt-1 cursor-pointer">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {bedroomOptions.map((option) => (
                    <SelectItem
                      key={option.value}
                      value={option.value}
                      className="cursor-pointer"
                    >
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Bathrooms */}
            <div>
              <Label className="text-sm font-medium">Baths</Label>
              <Select
                value={localFilters.bathrooms}
                onValueChange={(value) =>
                  updateLocalFilters({ bathrooms: value })
                }
              >
                <SelectTrigger className="mt-1 cursor-pointer">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {bathroomOptions.map((option) => (
                    <SelectItem
                      key={option.value}
                      value={option.value}
                      className="cursor-pointer"
                    >
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>

          {/* Property Types */}
          <div>
            <Label className="text-sm font-medium">Property Type</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-between mt-1 font-normal text-sm cursor-pointer"
                >
                  {localFilters.propertyTypes.length > 0
                    ? `${localFilters.propertyTypes.length} selected`
                    : "Any"}
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-full p-3">
                <div className="space-y-2">
                  {propertyTypes.map((type) => (
                    <div key={type.id} className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        id={`mobile-${type.id}`}
                        checked={localFilters.propertyTypes.includes(type.id)}
                        onChange={() => togglePropertyType(type.id)}
                        className="rounded cursor-pointer"
                      />
                      <Label
                        htmlFor={`mobile-${type.id}`}
                        className="text-sm cursor-pointer"
                      >
                        {type.label}
                      </Label>
                    </div>
                  ))}
                </div>
              </PopoverContent>
            </Popover>
          </div>

          {/* Cities */}
          <div>
            <Label className="text-sm font-medium">City</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-between mt-1 font-normal text-sm cursor-pointer"
                >
                  {localFilters.cities.length > 0
                    ? `${localFilters.cities.length} selected`
                    : "Any"}
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-full p-3">
                <div className="space-y-2">
                  {cities.map((city) => (
                    <div
                      key={city.value}
                      className="flex items-center space-x-2"
                    >
                      <input
                        type="checkbox"
                        id={`mobile-${city.value}`}
                        checked={localFilters.cities.includes(city.value)}
                        onChange={() => toggleCity(city.value)}
                        className="rounded cursor-pointer"
                      />
                      <Label
                        htmlFor={`mobile-${city.value}`}
                        className="text-sm cursor-pointer"
                      >
                        {city.label}
                      </Label>
                    </div>
                  ))}
                </div>
              </PopoverContent>
            </Popover>
          </div>

          {/* Zip Codes */}
          <div>
            <Label className="text-sm font-medium">Zip</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-between mt-1 font-normal text-sm cursor-pointer"
                >
                  {localFilters.zipCodes.length > 0
                    ? `${localFilters.zipCodes.length} selected`
                    : "Any"}
                  <ChevronDown className="h-4 w-4" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-full p-3 max-h-60 overflow-y-auto">
                <div className="space-y-2">
                  {zipCodes.map((zipCode) => (
                    <div key={zipCode} className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        id={`mobile-${zipCode}`}
                        checked={localFilters.zipCodes.includes(zipCode)}
                        onChange={() => toggleZipCode(zipCode)}
                        className="rounded cursor-pointer"
                      />
                      <Label
                        htmlFor={`mobile-${zipCode}`}
                        className="text-sm cursor-pointer"
                      >
                        {zipCode}
                      </Label>
                    </div>
                  ))}
                </div>
              </PopoverContent>
            </Popover>
          </div>

          {/* Availability Date */}
          <div>
            <Label className="text-sm font-medium">Available By</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-start mt-1 font-normal text-sm cursor-pointer"
                >
                  <CalendarIcon className="mr-2 h-4 w-4" />
                  {localFilters.availabilityDate
                    ? format(localFilters.availabilityDate, "MMM d")
                    : "Any date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <Calendar
                  mode="single"
                  selected={localFilters.availabilityDate}
                  onSelect={(date) =>
                    updateLocalFilters({ availabilityDate: date })
                  }
                  initialFocus
                />
                {localFilters.availabilityDate && (
                  <div className="p-2 border-t">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() =>
                        updateLocalFilters({ availabilityDate: undefined })
                      }
                      className="w-full text-sm cursor-pointer"
                    >
                      Clear date
                    </Button>
                  </div>
                )}
              </PopoverContent>
            </Popover>
          </div>

          {/* Mobile Apply Button */}
          <Button onClick={applyFilters} className="w-full cursor-pointer">
            <Search className="w-4 h-4 mr-2" />
            Apply Filters{" "}
            {getActiveFilterCount() > 0 && `(${getActiveFilterCount()})`}
          </Button>

          {/* Mobile Reset Button */}
          {getActiveFilterCount() > 0 && (
            <Button
              variant="outline"
              onClick={resetFilters}
              className="w-full cursor-pointer"
            >
              <RotateCcw className="w-4 h-4 mr-2" />
              Reset Filters
            </Button>
          )}
        </div>

        {/* Desktop Layout */}
        <div className="hidden md:grid md:grid-cols-8 lg:grid-cols-12 gap-4 items-end">
          {/* Price Range */}
          <div className="md:col-span-2 lg:col-span-2">
            <Label className="text-xs">Price Range</Label>
            <div className="mt-1">
              <Slider
                value={localFilters.priceRange}
                onValueChange={(value) =>
                  updateLocalFilters({ priceRange: value as [number, number] })
                }
                min={600}
                max={15000}
                step={50}
                className="w-full cursor-pointer"
              />
              <div className="flex justify-between mt-1 text-xs text-muted-foreground">
                <span>${localFilters.priceRange[0].toLocaleString()}</span>
                <span>${localFilters.priceRange[1].toLocaleString()}</span>
              </div>
            </div>
          </div>

          {/* Area Range */}
          <div className="md:col-span-2 lg:col-span-2">
            <Label className="text-xs">Area Range</Label>
            <div className="mt-1">
              <Slider
                value={localFilters.areaRange}
                onValueChange={(value) =>
                  updateLocalFilters({ areaRange: value as [number, number] })
                }
                min={150}
                max={5100}
                step={50}
                className="w-full cursor-pointer"
              />
              <div className="flex justify-between mt-1 text-xs text-muted-foreground">
                <span>{localFilters.areaRange[0].toLocaleString()}</span>
                <span>{localFilters.areaRange[1].toLocaleString()}</span>
              </div>
            </div>
          </div>

          {/* Bedrooms */}
          <div className="md:col-span-1 lg:col-span-1">
            <Label className="text-xs">Beds</Label>
            <Select
              value={localFilters.bedrooms}
              onValueChange={(value) => updateLocalFilters({ bedrooms: value })}
            >
              <SelectTrigger className="h-9 cursor-pointer">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {bedroomOptions.map((option) => (
                  <SelectItem
                    key={option.value}
                    value={option.value}
                    className="cursor-pointer"
                  >
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {/* Bathrooms */}
          <div className="md:col-span-1 lg:col-span-1">
            <Label className="text-xs">Baths</Label>
            <Select
              value={localFilters.bathrooms}
              onValueChange={(value) =>
                updateLocalFilters({ bathrooms: value })
              }
            >
              <SelectTrigger className="h-9 cursor-pointer">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {bathroomOptions.map((option) => (
                  <SelectItem
                    key={option.value}
                    value={option.value}
                    className="cursor-pointer"
                  >
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {/* Property Types */}
          <div className="md:col-span-1 lg:col-span-1">
            <Label className="text-xs">Type</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="h-9 w-full justify-between font-normal text-sm mt-1 cursor-pointer"
                >
                  {localFilters.propertyTypes.length > 0
                    ? `${localFilters.propertyTypes.length} selected`
                    : "Any"}
                  <ChevronDown className="h-3 w-3" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-48 p-2" align="start">
                <div className="space-y-2">
                  {propertyTypes.map((type) => (
                    <div key={type.id} className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        id={type.id}
                        checked={localFilters.propertyTypes.includes(type.id)}
                        onChange={() => togglePropertyType(type.id)}
                        className="rounded border border-input cursor-pointer"
                      />
                      <Label
                        htmlFor={type.id}
                        className="text-xs cursor-pointer"
                      >
                        {type.label}
                      </Label>
                    </div>
                  ))}
                </div>
              </PopoverContent>
            </Popover>
          </div>

          {/* Cities */}
          <div className="md:col-span-1 lg:col-span-1">
            <Label className="text-xs">City</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="h-9 w-full justify-between font-normal text-sm cursor-pointer"
                >
                  {localFilters.cities.length > 0
                    ? `${localFilters.cities.length} selected`
                    : "Any"}
                  <ChevronDown className="h-3 w-3" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-48 p-2" align="start">
                <div className="space-y-2">
                  {cities.map((city) => (
                    <div
                      key={city.value}
                      className="flex items-center space-x-2"
                    >
                      <input
                        type="checkbox"
                        id={`desktop-${city.value}`}
                        checked={localFilters.cities.includes(city.value)}
                        onChange={() => toggleCity(city.value)}
                        className="rounded border border-input cursor-pointer"
                      />
                      <Label
                        htmlFor={`desktop-${city.value}`}
                        className="text-xs cursor-pointer"
                      >
                        {city.label}
                      </Label>
                    </div>
                  ))}
                </div>
              </PopoverContent>
            </Popover>
          </div>

          {/* Zip Codes */}
          <div className="md:col-span-1 lg:col-span-1">
            <Label className="text-xs">Zip</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="h-9 w-full justify-between font-normal text-sm cursor-pointer"
                >
                  {localFilters.zipCodes.length > 0
                    ? `${localFilters.zipCodes.length} selected`
                    : "Any"}
                  <ChevronDown className="h-3 w-3" />
                </Button>
              </PopoverTrigger>
              <PopoverContent
                className="w-48 p-2 max-h-60 overflow-y-auto"
                align="start"
              >
                <div className="space-y-2">
                  {zipCodes.map((zipCode) => (
                    <div key={zipCode} className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        id={`desktop-${zipCode}`}
                        checked={localFilters.zipCodes.includes(zipCode)}
                        onChange={() => toggleZipCode(zipCode)}
                        className="rounded border border-input cursor-pointer"
                      />
                      <Label
                        htmlFor={`desktop-${zipCode}`}
                        className="text-xs cursor-pointer"
                      >
                        {zipCode}
                      </Label>
                    </div>
                  ))}
                </div>
              </PopoverContent>
            </Popover>
          </div>

          {/* Availability Date */}
          <div className="md:col-span-1 lg:col-span-2">
            <Label className="text-xs">Available By</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="h-9 w-full justify-start font-normal text-sm cursor-pointer"
                >
                  <CalendarIcon className="mr-2 h-4 w-4" />
                  {localFilters.availabilityDate
                    ? format(localFilters.availabilityDate, "MMM d")
                    : "Any date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <Calendar
                  mode="single"
                  selected={localFilters.availabilityDate}
                  onSelect={(date) =>
                    updateLocalFilters({ availabilityDate: date })
                  }
                  initialFocus
                />
                {localFilters.availabilityDate && (
                  <div className="p-2 border-t">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() =>
                        updateLocalFilters({ availabilityDate: undefined })
                      }
                      className="w-full text-sm cursor-pointer"
                    >
                      Clear date
                    </Button>
                  </div>
                )}
              </PopoverContent>
            </Popover>
          </div>

          {/* Apply Button */}
          <div className="md:col-span-1 lg:col-span-1">
            <Button
              onClick={applyFilters}
              className="h-9 w-full text-sm cursor-pointer"
            >
              <Search className="w-4 h-4" />
              Apply
            </Button>
          </div>
        </div>

        {/* Active Filters & Reset Button - NO cursor-pointer for badges */}
        {getActiveFilterCount() > 0 && (
          <div className="hidden md:flex justify-between items-center mt-4 pt-4 border-t">
            <div className="flex items-center gap-2">
              <span className="text-sm text-muted-foreground">
                Active filters:
              </span>
              {localFilters.cities.map((city) => (
                <Badge
                  key={`city-${city}`}
                  variant="secondary"
                  className="text-xs"
                >
                  {cities.find((c) => c.value === city)?.label}
                </Badge>
              ))}
              {localFilters.zipCodes.map((zip) => (
                <Badge
                  key={`zip-${zip}`}
                  variant="secondary"
                  className="text-xs"
                >
                  {zip}
                </Badge>
              ))}
              {localFilters.propertyTypes.map((type) => (
                <Badge
                  key={`type-${type}`}
                  variant="secondary"
                  className="text-xs"
                >
                  {propertyTypes.find((t) => t.id === type)?.label}
                </Badge>
              ))}
              {(localFilters.priceRange[0] !== 600 ||
                localFilters.priceRange[1] !== 15000) && (
                <Badge variant="secondary" className="text-xs">
                  ${localFilters.priceRange[0].toLocaleString()} - $
                  {localFilters.priceRange[1].toLocaleString()}
                </Badge>
              )}
              {(localFilters.areaRange[0] !== 150 ||
                localFilters.areaRange[1] !== 5100) && (
                <Badge variant="secondary" className="text-xs">
                  {localFilters.areaRange[0]}-{localFilters.areaRange[1]} sq ft
                </Badge>
              )}
              {localFilters.bedrooms !== "any" && (
                <Badge variant="secondary" className="text-xs">
                  {
                    bedroomOptions.find(
                      (b) => b.value === localFilters.bedrooms
                    )?.label
                  }
                </Badge>
              )}
              {localFilters.bathrooms !== "any" && (
                <Badge variant="secondary" className="text-xs">
                  {
                    bathroomOptions.find(
                      (b) => b.value === localFilters.bathrooms
                    )?.label
                  }
                </Badge>
              )}
              {localFilters.availabilityDate && (
                <Badge variant="secondary" className="text-xs">
                  Available by {format(localFilters.availabilityDate, "MMM d")}
                </Badge>
              )}
            </div>
            <Button
              variant="outline"
              onClick={resetFilters}
              className="h-8 px-3 text-xs cursor-pointer"
            >
              <RotateCcw className="w-3 h-3 mr-1" />
              Reset Filters
            </Button>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
