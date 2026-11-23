#!/usr/bin/env python3
"""aeroClient installer: creates a separate 'aeroClient' version + profile in the
official Minecraft launcher (own game folder, Fabric Loader + Fabric API + aeroClient).
Needs internet and Python 3. Usage: python install.py [path/to/aeroclient-0.1.0.jar]"""
import json, os, sys, shutil, urllib.request, urllib.parse, glob, platform

MC, LOADER, NAME = "1.21.4", "0.16.10", "aeroClient"
VID = f"{NAME}-{MC}"

def mc_dir():
    s = platform.system()
    if s == "Windows": return os.path.join(os.environ["APPDATA"], ".minecraft")
    if s == "Darwin": return os.path.expanduser("~/Library/Application Support/minecraft")
    return os.path.expanduser("~/.minecraft")

def get(url):
    req = urllib.request.Request(url, headers={"User-Agent": "aeroClient-installer"})
    return urllib.request.urlopen(req, timeout=60).read()

def main():
    jar = sys.argv[1] if len(sys.argv) > 1 else (glob.glob("build/libs/aeroclient-*.jar") or [None])[0]
    if not jar or "sources" in jar or not os.path.exists(jar):
        sys.exit("aeroclient jar not found. Run 'gradle build' first or pass the jar path.")
    base = mc_dir()
    game = os.path.join(base, "aeroClient")
    mods = os.path.join(game, "mods")
    os.makedirs(mods, exist_ok=True)

    # 1) Fabric profile as its own version
    prof = json.loads(get(f"https://meta.fabricmc.net/v2/versions/loader/{MC}/{LOADER}/profile/json"))
    prof["id"] = VID
    vdir = os.path.join(base, "versions", VID)
    os.makedirs(vdir, exist_ok=True)
    with open(os.path.join(vdir, VID + ".json"), "w") as f: json.dump(prof, f, indent=2)

    # 2) Fabric API from Modrinth
    q = urllib.parse.urlencode({"game_versions": json.dumps([MC]), "loaders": json.dumps(["fabric"])})
    vers = json.loads(get(f"https://api.modrinth.com/v2/project/fabric-api/version?{q}"))
    f0 = vers[0]["files"][0]
    for old in glob.glob(os.path.join(mods, "fabric-api-*.jar")): os.remove(old)
    with open(os.path.join(mods, f0["filename"]), "wb") as f: f.write(get(f0["url"]))

    # 3) aeroClient jar
    for old in glob.glob(os.path.join(mods, "aeroclient-*.jar")): os.remove(old)
    shutil.copy(jar, mods)

    # 4) launcher profile
    lp = os.path.join(base, "launcher_profiles.json")
    data = json.load(open(lp)) if os.path.exists(lp) else {"profiles": {}}
    data.setdefault("profiles", {})[NAME] = {"name": NAME, "type": "custom", "lastVersionId": VID,
                                             "gameDir": game, "icon": "Diamond_Block"}
    json.dump(data, open(lp, "w"), indent=2)
    print("Done. Open the Minecraft launcher and pick the 'aeroClient' profile (needs Java 21).")

main()
