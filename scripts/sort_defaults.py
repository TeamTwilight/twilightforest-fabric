defaults = "src/main/resources/assets/emi/recipe/defaults/twilightforest.json"
lines = []
file = open(defaults)
for line in file:
    if line not in lines:
        if '"' in line:
            if "," not in line:
                line = line + ","
            lines.append(line)
lines.sort()
out = open(defaults + ".new", "a")
out.writelines(lines)