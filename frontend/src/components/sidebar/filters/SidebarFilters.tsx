"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Slider } from "@/components/ui/slider";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { FilterIcon, CalendarIcon } from "lucide-react";
import { Calendar } from "@/components/ui/calendar";

export interface FilterState {
  priceRange: [number, number];
  areaRange: [number, number];
  cities: string[];
  zipCodes: string[];
  bedrooms: string;
  bathrooms: string;
  propertyTypes: string[];
  availabilityDate: Date | undefined;
}

interface PropertyFiltersProps {
  onFiltersChange?: (filters: FilterState) => void;
  onApplyFilters?: (filters: FilterState) => void;
  className?: string;
}

const defaultFilters: FilterState = {
  priceRange: [600, 15000],
  areaRange: [150, 5100],
  cities: [],
  zipCodes: [],
  bedrooms: "any",
  bathrooms: "any",
  propertyTypes: [],
  availabilityDate: undefined,
};

export function SidebarFilters({
  onFiltersChange,
  onApplyFilters,
  className = "",
}: PropertyFiltersProps) {
  const [filters, setFilters] = useState<FilterState>(defaultFilters);

  const cities = [
    { value: "orlando", label: "Orlando" },
    { value: "oviedo", label: "Oviedo" },
    { value: "winter-park", label: "Winter Park" },
    { value: "winter-springs", label: "Winter Springs" },
  ];

  const zipCodes = [
    "32708",
    "32765",
    "32766",
    "32792",
    "32817",
    "32820",
    "32824",
    "32825",
    "32826",
    "32827",
    "32828",
    "32829",
    "32833",
  ];

  const propertyTypes = [
    { id: "apartment", label: "Apartment" },
    { id: "condo", label: "Condo" },
    { id: "townhouse", label: "Townhouse" },
    { id: "house", label: "House" },
  ];

  const bedroomOptions = [
    { value: "any", label: "Any" },
    { value: "0", label: "Studio" },
    { value: "1", label: "1 bed" },
    { value: "2", label: "2 beds" },
    { value: "3", label: "3 beds" },
    { value: "4", label: "4 beds" },
    { value: "5", label: "5 beds" },
    { value: "6", label: "6 beds" },
    { value: "7", label: "7 beds" },
    { value: "8", label: "8+ beds" },
  ];

  const bathroomOptions = [
    { value: "any", label: "Any" },
    { value: "1", label: "1 bath" },
    { value: "2", label: "2 baths" },
    { value: "3", label: "3 baths" },
    { value: "4", label: "4 baths" },
    { value: "5", label: "5 baths" },
    { value: "6", label: "6+ baths" },
  ];

  const updateFilters = (newFilters: Partial<FilterState>) => {
    const updated = { ...filters, ...newFilters };
    setFilters(updated);
    onFiltersChange?.(updated);
  };

  const handleFilterReset = () => {
    setFilters(defaultFilters);
    onFiltersChange?.(defaultFilters);
  };

  const handleApplyFilters = () => {
    onApplyFilters?.(filters);
  };

  const handleArrayToggle = (
    array: string[],
    value: string,
    key: keyof Pick<FilterState, "cities" | "zipCodes" | "propertyTypes">
  ) => {
    const newArray = array.includes(value)
      ? array.filter((item) => item !== value)
      : [...array, value];

    updateFilters({ [key]: newArray });
  };

  const formatDate = (date: Date | undefined) => {
    if (!date) return "Select date";
    return date.toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
    });
  };

  return (
    <div className={`space-y-6 ${className}`}>
      {/* Price Range */}
      <div className="space-y-4">
        <Label className=" font-semibold">Price Range</Label>
        <div className="space-y-4">
          <div className="px-4">
            <Slider
              value={filters.priceRange}
              onValueChange={(value) =>
                updateFilters({ priceRange: value as [number, number] })
              }
              max={15000}
              min={600}
              step={50}
              className="w-full"
            />
          </div>
          <div className="flex justify-between text-xs text-muted-foreground px-2">
            <span>${filters.priceRange[0]}/mo</span>
            <span>${filters.priceRange[1]}/mo</span>
          </div>
        </div>
      </div>

      {/* Area Range */}
      <div className="space-y-4">
        <Label className="font-semibold">Square Footage</Label>
        <div className="space-y-4">
          <div className="px-4">
            <Slider
              value={filters.areaRange}
              onValueChange={(value) =>
                updateFilters({ areaRange: value as [number, number] })
              }
              max={5100}
              min={150}
              step={50}
              className="w-full"
            />
          </div>
          <div className="flex justify-between text-xs text-muted-foreground px-2">
            <span>{filters.areaRange[0]} sq ft</span>
            <span>{filters.areaRange[1]} sq ft</span>
          </div>
        </div>
      </div>

      {/* Bedrooms & Bathrooms */}
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-3">
          <Label className="font-semibold">Bedrooms</Label>
          <Select
            value={filters.bedrooms}
            onValueChange={(value) => updateFilters({ bedrooms: value })}
          >
            <SelectTrigger className="h-10">
              <SelectValue placeholder="Any" />
            </SelectTrigger>
            <SelectContent>
              {bedroomOptions.map((option) => (
                <SelectItem key={option.value} value={option.value}>
                  {option.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <div className="space-y-3">
          <Label className="font-semibold">Bathrooms</Label>
          <Select
            value={filters.bathrooms}
            onValueChange={(value) => updateFilters({ bathrooms: value })}
          >
            <SelectTrigger className="h-10">
              <SelectValue placeholder="Any" />
            </SelectTrigger>
            <SelectContent>
              {bathroomOptions.map((option) => (
                <SelectItem key={option.value} value={option.value}>
                  {option.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      {/* Cities */}
      <div className="space-y-4">
        <Label className="font-semibold">Cities</Label>
        <div className="space-y-3 max-h-40 overflow-y-auto border rounded-md p-3 bg-muted/30">
          {cities.map((city) => (
            <div key={city.value} className="flex items-center space-x-3">
              <Checkbox
                id={`city-${city.value}`}
                checked={filters.cities.includes(city.value)}
                onCheckedChange={() =>
                  handleArrayToggle(filters.cities, city.value, "cities")
                }
              />
              <Label
                htmlFor={`city-${city.value}`}
                className="text-sm font-normal cursor-pointer flex-1"
              >
                {city.label}
              </Label>
            </div>
          ))}
        </div>
      </div>

      {/* Zip Codes */}
      <div className="space-y-4">
        <Label className="font-semibold">Zip Codes</Label>
        <div className="grid grid-cols-2 gap-2 max-h-40 overflow-y-auto border rounded-md p-3 bg-muted/30">
          {zipCodes.map((zip) => (
            <div key={zip} className="flex items-center space-x-2">
              <Checkbox
                id={`zip-${zip}`}
                checked={filters.zipCodes.includes(zip)}
                onCheckedChange={() =>
                  handleArrayToggle(filters.zipCodes, zip, "zipCodes")
                }
                className="h-4 w-4"
              />
              <Label
                htmlFor={`zip-${zip}`}
                className="text-xs font-normal cursor-pointer"
              >
                {zip}
              </Label>
            </div>
          ))}
        </div>
      </div>

      {/* Property Types */}
      <div className="space-y-4">
        <Label className="font-semibold">Property Type</Label>
        <div className="space-y-3 border rounded-md p-3 bg-muted/30">
          {propertyTypes.map((type) => (
            <div key={type.id} className="flex items-center space-x-3">
              <Checkbox
                id={`property-${type.id}`}
                checked={filters.propertyTypes.includes(type.id)}
                onCheckedChange={() =>
                  handleArrayToggle(
                    filters.propertyTypes,
                    type.id,
                    "propertyTypes"
                  )
                }
              />
              <Label
                htmlFor={`property-${type.id}`}
                className="text-sm font-normal cursor-pointer flex-1"
              >
                {type.label}
              </Label>
            </div>
          ))}
        </div>
      </div>

      {/* Availability Date */}
      <div className="space-y-4">
        <Label className="font-semibold">Available By</Label>
        <Popover>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              className="w-full h-10 justify-start text-left font-normal"
            >
              <CalendarIcon className="mr-2 h-4 w-4" />
              {formatDate(filters.availabilityDate)}
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto p-0" align="start">
            <Calendar
              mode="single"
              selected={filters.availabilityDate}
              onSelect={(date) => updateFilters({ availabilityDate: date })}
              disabled={(date) => date < new Date()}
              initialFocus
            />
          </PopoverContent>
        </Popover>
      </div>

      {/* Active Filters Summary */}
      {(filters.cities.length > 0 ||
        filters.zipCodes.length > 0 ||
        filters.propertyTypes.length > 0 ||
        filters.bedrooms !== "any" ||
        filters.bathrooms !== "any" ||
        filters.availabilityDate) && (
        <div className="space-y-3">
          <Label className="font-semibold">Active Filters</Label>
          <div className="flex flex-wrap gap-2">
            {filters.cities.map((city) => (
              <span
                key={city}
                className="inline-flex items-center px-2 py-1 bg-primary/10 text-primary text-xs rounded-md"
              >
                {cities.find((c) => c.value === city)?.label}
              </span>
            ))}
            {filters.zipCodes.map((zip) => (
              <span
                key={zip}
                className="inline-flex items-center px-2 py-1 bg-primary/10 text-primary text-xs rounded-md"
              >
                {zip}
              </span>
            ))}
            {filters.propertyTypes.map((type) => (
              <span
                key={type}
                className="inline-flex items-center px-2 py-1 bg-primary/10 text-primary text-xs rounded-md"
              >
                {propertyTypes.find((t) => t.id === type)?.label}
              </span>
            ))}
            {filters.bedrooms !== "any" && (
              <span className="inline-flex items-center px-2 py-1 bg-primary/10 text-primary text-xs rounded-md">
                {
                  bedroomOptions.find((b) => b.value === filters.bedrooms)
                    ?.label
                }
              </span>
            )}
            {filters.bathrooms !== "any" && (
              <span className="inline-flex items-center px-2 py-1 bg-primary/10 text-primary text-xs rounded-md">
                {
                  bathroomOptions.find((b) => b.value === filters.bathrooms)
                    ?.label
                }
              </span>
            )}
            {filters.availabilityDate && (
              <span className="inline-flex items-center px-2 py-1 bg-primary/10 text-primary text-xs rounded-md">
                Available by {formatDate(filters.availabilityDate)}
              </span>
            )}
          </div>
        </div>
      )}

      {/* Filter Actions - Sticky at bottom */}
      <div className="sticky bottom-0">
        <div className="flex gap-3">
          <Button
            size="default"
            className="flex-1 h-11"
            onClick={handleApplyFilters}
          >
            Apply Filters
          </Button>
          <Button
            variant="outline"
            size="default"
            onClick={handleFilterReset}
            className="px-6 h-11"
          >
            Reset
          </Button>
        </div>
      </div>
    </div>
  );
}
